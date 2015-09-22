package com.github.mjdbc;

import com.github.mjdbc.mapper.ListMapper;
import com.github.mjdbc.mapper.VoidMapper;
import com.github.mjdbc.util.Binders;
import com.github.mjdbc.util.Mappers;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.reflect.Modifier.isFinal;
import static java.lang.reflect.Modifier.isPublic;
import static java.lang.reflect.Modifier.isStatic;
import static java.lang.reflect.Modifier.isTransient;
import static java.util.Objects.requireNonNull;

/**
 * Main class of the library: implementation for Db interface.
 */
public class DbImpl implements Db {

    private final ThreadLocal<DbConnection> activeConnections = new ThreadLocal<>();

    private final Map<Method, OpRef> opByMethod = new ConcurrentHashMap<>();

    private final Map<Class, DbMapper> mapperByClass = new ConcurrentHashMap<>(Mappers.BUILT_IN_MAPPERS);

    private final Map<Class, DbBinder> binderByClass = new ConcurrentHashMap<>(Binders.BUILT_IN_BINDERS);

    private final Map<Class, List<BindInfo>> beanInfoByClass = new ConcurrentHashMap<>();

    private final Map<Method, DbTimer> timersByMethod = new ConcurrentHashMap<>();

    @NotNull
    private final DataSource dataSource;

    public DbImpl(@NotNull DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public <T> void registerMapper(@NotNull Class<T> mapperClass, @NotNull DbMapper<T> mapper) {
        requireNonNull(mapperClass);
        requireNonNull(mapper);
        if (Collection.class.isAssignableFrom(mapperClass)) {
            throw new IllegalArgumentException("Collection mappers override is not supported.");
        }
        DbMapper oldMapper = mapperByClass.put(mapperClass, mapper);
        if (oldMapper != null) { //update all old cached mappings
            opByMethod.values().stream().filter(r -> r.resultMapper.equals(oldMapper)).forEach(r -> r.resultMapper = mapper);
        }
    }

    @Override
    public <T> void registerBinder(@NotNull Class<? extends T> binderClass, @NotNull DbBinder<T> binder) {
        requireNonNull(binderClass);
        requireNonNull(binder);
        binderByClass.put(binderClass, binder);
    }

    @NotNull
    public <T> T attachDbi(@NotNull final T impl, @NotNull Class<T> daoInterface) {
        requireNonNull(impl);
        requireNonNull(daoInterface);

        //noinspection unchecked
        return (T) Proxy.newProxyInstance(impl.getClass().getClassLoader(), new Class[]{daoInterface}, new DbiProxy<>(impl));
    }


    @Nullable
    public <T> T execute(@NotNull DbOp<T> op) {
        return executeImpl(op);
    }

    @NotNull
    public <T> T executeNN(@NotNull DbOpNN<T> op) {
        return executeImpl(op);
    }

    public void executeV(@NotNull DbOpV op) {
        executeImpl(op);
    }

    @Override
    public Map<Method, DbTimer> getTimers() {
        return timersByMethod;
    }

    @SuppressWarnings("unchecked")
    private <T> T executeImpl(@NotNull Object op) {
        requireNonNull(op);
        boolean topLevel = false;
        DbConnection c = activeConnections.get();
        try {
            try {
                if (c == null) {
                    c = new DbConnection(dataSource.getConnection());
                    activeConnections.set(c);
                    topLevel = true;
                }
                T res = (T) run(op, c);
                if (topLevel) {
                    c.commit();
                }
                return res;
            } catch (Exception e) {
                if (topLevel) {
                    c.rollback();
                }
                if (e instanceof RuntimeException) {
                    throw (RuntimeException) e;
                } else if (e instanceof InvocationTargetException && e.getCause() instanceof RuntimeException) {
                    throw (RuntimeException) e.getCause();
                }
                throw new RuntimeException(e);
            } finally {
                if (topLevel) {
                    activeConnections.set(null);
                    c.close();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Object run(@NotNull Object op, @NotNull DbConnection c) throws Exception {
        if (op instanceof DbOp) {
            return ((DbOp) op).run(c);
        } else if (op instanceof DbOpNN) {
            return ((DbOpNN) op).run(c);
        }
        ((DbOpV) op).run(c);
        return null;
    }


    @NotNull
    @Override
    public <T> T attachSql(@NotNull Class<T> sqlInterface) {
        requireNonNull(sqlInterface);
        if (!sqlInterface.isInterface()) {
            throw new IllegalArgumentException("Not interface: " + sqlInterface);
        }
        for (Method m : sqlInterface.getMethods()) {
            registerMethod(m);
        }
        //noinspection unchecked
        return (T) Proxy.newProxyInstance(sqlInterface.getClassLoader(), new Class[]{sqlInterface}, new SqlProxy());
    }

    private void registerMethod(@NotNull Method m) {
        // return type
        DbMapper resultMapper = null;
        Class<?> returnType = m.getReturnType();
        if (returnType == List.class) {
            ParameterizedType genericReturnType = (ParameterizedType) m.getGenericReturnType();
            Class<?> elementClass = (Class<?>) genericReturnType.getActualTypeArguments()[0];
            DbMapper elementMapper = mapperByClass.get(elementClass);
            if (elementMapper != null) {
                resultMapper = new ListMapper(elementMapper);
            }
        } else {
            resultMapper = mapperByClass.get(returnType);
        }
        if (resultMapper == null) {
            throw new IllegalArgumentException("Not supported query result type: " + returnType);
        }

        // sql named params
        Sql sqlAnnotation = m.getAnnotation(Sql.class);
        if (sqlAnnotation == null) {
            throw new IllegalArgumentException("Method has no @Sql annotation: " + m);
        }
        String sql = sqlAnnotation.value();
        if (sql.isEmpty()) {
            throw new IllegalArgumentException("Illegal SQL: '" + sql + "' in method: " + m);
        }
        Map<String, List<Integer>> parametersMapping = new HashMap<>();
        String parsedSql = DbStatement.parse(sql, parametersMapping);

        // parameter bindings
        List<BindInfo> bindings = new ArrayList<>();
        Class<?>[] parameterTypes = m.getParameterTypes();
        for (int i = 0; i < m.getParameterCount(); i++) {
            Class<?> parameterType = parameterTypes[i];
            Bind bindAnnotation = (Bind) Arrays.stream(m.getParameterAnnotations()[i]).filter(a -> a instanceof Bind).findFirst().orElse(null);
            if (bindAnnotation == null) {
                BindBean bindBeanAnnotation = (BindBean) Arrays.stream(m.getParameterAnnotations()[i]).filter(a -> a instanceof BindBean).findFirst().orElse(null);
                if (bindBeanAnnotation == null) {
                    throw new IllegalArgumentException("Unbound parameter: " + i + ", method: " + m);
                }
                if (m.getParameterCount() != 1) {
                    throw new IllegalArgumentException("@BindBean must be the only parameter! Method: " + m);
                }
                bindings.addAll(getBeanBinders(parameterType));
                break;
            }
            String name = bindAnnotation.value();
            if (name.isEmpty()) {
                throw new IllegalArgumentException("Parameter name is empty! Position" + i + ", method: " + m);
            }
            DbBinder binder = findBinderByType(parameterType);
            if (binder == null) {
                throw new IllegalArgumentException("No parameter binder for: '" + parameterType + "', method: " + m + ", position: " + i + ", type: " + parameterType);
            }
            bindings.add(new BindInfo(name, binder, i, null, null));
        }

        // check that all parameters are bound
        for (String name : parametersMapping.keySet()) {
            BindInfo info = bindings.stream().filter(b -> b.mappedName.equals(name)).findAny().orElse(null);
            if (info == null) {
                throw new IllegalArgumentException("No binding found for '" + name + "', method: " + m);
            }
        }

        boolean useGeneratedKeys = returnType.getAnnotation(UseGeneratedKeys.class) != null || sql.toUpperCase().startsWith("INSERT ");

        // construct result mapping
        opByMethod.put(m, new OpRef(parsedSql, resultMapper, useGeneratedKeys, parametersMapping, bindings.toArray(new BindInfo[bindings.size()])));
    }

    /**
     * For all public fields and getters assign binders by type
     */
    private List<BindInfo> getBeanBinders(Class<?> type) {
        List<BindInfo> bindings = beanInfoByClass.get(type);
        if (bindings != null) {
            return bindings;
        }
        bindings = new ArrayList<>();

        // check all public fields
        for (Field f : type.getFields()) {
            int modifiers = f.getModifiers();
            if (isFinal(modifiers) || !isPublic(modifiers) || isStatic(modifiers) || isTransient(modifiers)) {
                continue; //ignore
            }
            DbBinder binder = findBinderByType(f.getType());
            if (binder == null) {
                throw new IllegalArgumentException("No bean binder for field: " + f + ", bean: " + type);
            }
            bindings.add(new BindInfo(f.getName(), binder, 0, f, null));
        }

        // check all public get/is methods
        for (Method m : type.getMethods()) {
            int modifiers = m.getModifiers();
            String methodName = m.getName();
            if (!isPublic(modifiers) || isStatic(modifiers) || !methodName.startsWith("get") || !methodName.startsWith("is")) {
                continue; //ignore
            }
            String suffix = methodName.startsWith("is") ? methodName.substring(2) : methodName.substring(3);
            String propertyName = Character.toLowerCase(suffix.charAt(0)) + suffix.substring(1);
            if (bindings.stream().filter(b -> b.mappedName.equals(propertyName)).findAny().orElse(null) != null) { // field has higher priority.
                continue;
            }
            DbBinder binder = findBinderByType(m.getReturnType());
            if (binder == null) {
                throw new IllegalArgumentException("No bean binder for method: " + m + ", bean: " + type);
            }
            bindings.add(new BindInfo(propertyName, binder, 0, null, m));
        }
        beanInfoByClass.put(type, bindings);
        return bindings;
    }

    @Nullable
    private DbBinder findBinderByType(Class<?> parameterType) {
        DbBinder binder = binderByClass.get(parameterType);
        if (binder == null) {
            binder = findBinderByInterfaces(parameterType.getInterfaces());
            if (binder == null) {
                Class superClass = parameterType.getSuperclass();
                binder = superClass == null ? null : findBinderByType(superClass);
            }
        }
        return binder;
    }

    private DbBinder findBinderByInterfaces(Class<?>[] interfaces) {
        for (Class interfaceClass : interfaces) {
            DbBinder binder = binderByClass.get(interfaceClass);
            if (binder == null) {
                binder = findBinderByInterfaces(interfaceClass.getInterfaces());
            }
            if (binder != null) {
                return binder;
            }
        }
        return null;
    }

    private static class OpRef {
        @NotNull
        final String parsedSql;

        @NotNull
        DbMapper resultMapper;

        @NotNull
        final Map<String, List<Integer>> parametersNamesMapping;

        @NotNull
        final BindInfo[] bindings;

        final boolean useGeneratedKeys;


        public OpRef(@NotNull String parsedSql, @NotNull DbMapper resultMapper, boolean useGeneratedKeys,
                     @NotNull Map<String, List<Integer>> parametersNamesMapping, @NotNull BindInfo[] bindings) {
            this.parsedSql = parsedSql;
            this.useGeneratedKeys = useGeneratedKeys;
            this.resultMapper = resultMapper;
            this.parametersNamesMapping = parametersNamesMapping;
            this.bindings = bindings;
        }
    }

    private static class BindInfo {
        @NotNull
        final String mappedName;

        @NotNull
        final DbBinder binder;

        final int argumentIdx;

        @Nullable
        final Field field;

        @Nullable
        final Method getter;

        public BindInfo(@NotNull String mappedName, @NotNull DbBinder binder, int argumentIdx, @Nullable Field field, @Nullable Method getter) {
            this.mappedName = mappedName;
            this.binder = binder;
            this.argumentIdx = argumentIdx;
            this.field = field;
            this.getter = getter;
        }

        @Override
        public String toString() {
            return "BinderInfo[n:'" + mappedName + "', b:" + binder + ", a:" + argumentIdx + ", f:" + field + ", g:" + getter + "]";
        }
    }

    private class SqlProxy implements InvocationHandler {
        @SuppressWarnings("unchecked")
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            OpRef p = opByMethod.get(method);
            long t0 = System.nanoTime();
            try {
                return DbImpl.this.execute(c -> {
                    DbStatement s = new DbStatement(c, p.parsedSql, p.resultMapper, p.parametersNamesMapping, p.useGeneratedKeys);
                    if (args != null) {
                        for (BindInfo bi : p.bindings) {
                            List<Integer> sqlIndexes = p.parametersNamesMapping.get(bi.mappedName);
                            if (sqlIndexes == null) {
                                continue;
                            }
                            for (Integer idx : sqlIndexes) {
                                Object arg = args[bi.argumentIdx];
                                Object value = bi.field != null ? bi.field.get(arg) : bi.getter != null ? bi.getter.invoke(arg) : arg;
                                bi.binder.bind(s.statement, idx, value);
                            }
                        }
                    }
                    if (p.resultMapper != VoidMapper.INSTANCE) {
                        if (p.useGeneratedKeys) {
                            return s.updateAndGetGeneratedKeys();
                        } else {
                            return s.query();
                        }
                    }
                    s.executeUpdate();
                    return null;
                });
            } finally {
                updateTimer(method, t0);
            }
        }
    }

    private class DbiProxy<T> implements InvocationHandler {
        private final T impl;

        public DbiProxy(T impl) {
            this.impl = impl;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (method.getAnnotation(Tx.class) != null) {
                long t0 = System.nanoTime();
                try {
                    //noinspection unchecked
                    return DbImpl.this.execute(c -> (T) method.invoke(impl, args));
                } finally {
                    updateTimer(method, t0);
                }
            } else {
                return method.invoke(impl, args);
            }
        }
    }

    private void updateTimer(Method method, long t0) {
        DbTimer timer = timersByMethod.get(method);
        if (timer == null) {
            synchronized (timersByMethod) {
                timer = timersByMethod.get(method);
                if (timer == null) {
                    timer = new DbTimer(method);
                    timersByMethod.put(method, timer);
                }
            }
        }
        timer.onInvoke(System.nanoTime() - t0);
    }
}
