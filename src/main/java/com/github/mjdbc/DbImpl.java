package com.github.mjdbc;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.sql.DataSource;
import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.reflect.Modifier.*;
import static java.util.Objects.requireNonNull;

/**
 * Main class of the library: implementation for Db interface.
 */
public class DbImpl implements Db {

    private final ThreadLocal<DbConnection> activeConnections = new ThreadLocal<>();

    final Map<Method, OpRef> opByMethod = new ConcurrentHashMap<>();

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
    public <T> T attachDbi(@NotNull final T impl, @NotNull Class<T> dbiInterface) {
        requireNonNull(impl);
        requireNonNull(dbiInterface);

        //noinspection unchecked
        return (T) Proxy.newProxyInstance(impl.getClass().getClassLoader(), new Class[]{dbiInterface}, new DbiProxy<>(this, impl));
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
                    c = new DbConnection(dataSource);
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
                    try {
                        c.close();
                    } catch (Exception ignored) {
                    }
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
        return (T) Proxy.newProxyInstance(sqlInterface.getClassLoader(), new Class[]{sqlInterface}, new SqlProxy(this, sqlInterface.getSimpleName()));
    }

    private void registerMethod(@NotNull Method m) {
        // return type
        DbMapper resultMapper;
        Class<?> returnType = m.getReturnType();
        if (returnType == List.class) {
            ParameterizedType genericReturnType = (ParameterizedType) m.getGenericReturnType();
            Class<?> elementType = (Class<?>) genericReturnType.getActualTypeArguments()[0];
            DbMapper elementMapper = findMapperByType(elementType);
            if (elementMapper != null) {
                resultMapper = new ListMapper(elementMapper);
            } else {
                throw new IllegalArgumentException(getNoMapperMessage(elementType));
            }
        } else {
            resultMapper = findMapperByType(returnType);
        }
        if (resultMapper == null) {
            throw new IllegalArgumentException(getNoMapperMessage(returnType));
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
        Type[] genericTypes = m.getGenericParameterTypes();
        int batchParamIdx = -1;
        BatchIteratorFactory batchIteratorFactory = null;
        for (int i = 0; i < m.getParameterCount(); i++) {
            Class<?> parameterType = parameterTypes[i];
            Type genericType = genericTypes[i];
            Bind bindAnnotation = (Bind) Arrays.stream(m.getParameterAnnotations()[i]).filter(a -> a instanceof Bind).findFirst().orElse(null);
            if (bindAnnotation == null) {
                BindBean bindBeanAnnotation = (BindBean) Arrays.stream(m.getParameterAnnotations()[i]).filter(a -> a instanceof BindBean).findFirst().orElse(null);
                if (bindBeanAnnotation == null) {
                    throw new IllegalArgumentException("Unbound parameter: " + i + ", method: " + m + ". Forgot @BindBean or @Bind annotation?");
                }
                if (m.getParameterCount() != 1) {
                    throw new IllegalArgumentException("@BindBean must be the only parameter! Method: " + m);
                }
                if (isBatchType(parameterType)) {
                    batchIteratorFactory = getBatchHandlerFactory(parameterType);
                    bindings.add(new BindInfo("", Binders.VoidBinder, -1, null, null));
                    batchParamIdx = 0;
                    Class<?> beanType = getEffectiveType(parameterType, genericType);
                    bindings.addAll(getBeanBinders(beanType));
                } else {
                    bindings.addAll(getBeanBinders(parameterType));
                }
                break;
            }
            String name = bindAnnotation.value();
            if (name.isEmpty()) {
                throw new IllegalArgumentException("Parameter name is empty! Position" + i + ", method: " + m);
            }
            DbBinder binder = findBinderByType(parameterType, genericType);
            if (binder == null) {
                throw new IllegalArgumentException("No parameter binder for: '" + parameterType + "', method: " + m + ", position: " + i + ", type: " + parameterType);
            }
            BatchIteratorFactory paramBatchIteratorFactory = getBatchHandlerFactory(parameterType);
            if (paramBatchIteratorFactory != null) {
                if (resultMapper != Mappers.VoidMapper) {
                    //todo: support return of original JDBC executeBatch results as int[]
                    throw new IllegalArgumentException("BatchChunkSize method must return no result (be void), method: " + m + ", position: " + i + ", type: " + parameterType);
                }
                if (batchParamIdx != -1) {
                    throw new IllegalArgumentException("Multiple batch mode parameters! , method: " + m + ", args positions " + batchParamIdx + " and " + i);
                }
                batchParamIdx = i;
                batchIteratorFactory = paramBatchIteratorFactory;
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

        int batchSize = sqlAnnotation.batchChunkSize();
        if (batchSize <= 0) {
            throw new IllegalArgumentException("Illegal batch size : " + m + " batch size:" + sqlAnnotation.batchChunkSize());
        }
        // construct result mapping
        BindInfo[] bindingsArray = bindings.toArray(new BindInfo[bindings.size()]);
        OpRef op = new OpRef(parsedSql, resultMapper, useGeneratedKeys, parametersMapping, bindingsArray, batchIteratorFactory, batchParamIdx, batchSize);
        opByMethod.put(m, op);
    }

    @NotNull
    private static String getNoMapperMessage(@NotNull Class<?> elementType) {
        return "Not supported query result type: " + elementType + "." +
                " Add MAPPER field or register custom DbMapper for the type.";
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
            DbBinder binder = findBinderByType(f.getType(), null);
            if (binder == null) {
                throw new IllegalArgumentException("No bean binder for field: " + f + ", bean: " + type);
            }
            bindings.add(new BindInfo(f.getName(), binder, 0, f, null));
        }

        // check all public get/is methods
        for (Method m : type.getMethods()) {
            int modifiers = m.getModifiers();
            String methodName = m.getName();
            if (!isPublic(modifiers) || isStatic(modifiers) || m.getParameterCount() != 0 || !(methodName.startsWith("get") || methodName.startsWith("is"))) {
                continue; //ignore
            }
            String suffix = methodName.startsWith("is") ? methodName.substring(2) : methodName.substring(3);
            String propertyName = Character.toLowerCase(suffix.charAt(0)) + suffix.substring(1);
            if (bindings.stream().filter(b -> b.mappedName.equals(propertyName)).findAny().orElse(null) != null) { // field has higher priority.
                continue;
            }
            DbBinder binder = findBinderByType(m.getReturnType(), null);
            if (binder != null) {
                bindings.add(new BindInfo(propertyName, binder, 0, null, m));
            }
        }
        beanInfoByClass.put(type, bindings);
        return bindings;
    }

    @Nullable
    private DbMapper findMapperByType(Class<?> type) {
        DbMapper mapper = mapperByClass.get(type);
        if (mapper == null) {
            // search for a field marked as mapper with valid parameter type
            for (Field f : type.getDeclaredFields()) {
                int mods = f.getModifiers();
                if (Modifier.isStatic(mods) && Modifier.isPublic(mods) && f.getType().isAssignableFrom(DbMapper.class)) {
                    AnnotatedType at = f.getAnnotatedType();
                    if (at instanceof AnnotatedParameterizedType) {
                        AnnotatedParameterizedType apt = (AnnotatedParameterizedType) at;
                        AnnotatedType[] args = apt.getAnnotatedActualTypeArguments();
                        if (args.length == 1 && args[0].getType() == type) {
                            Mapper a = f.getAnnotation(Mapper.class);
                            if (a != null || f.getName().equals("MAPPER")) {
                                try {
                                    mapper = (DbMapper) f.get(type);
                                } catch (IllegalAccessException ignored) { // already checked for 'isPublic' before
                                }
                                if (mapperByClass.containsKey(type)) {
                                    throw new IllegalArgumentException("Found multiple mappers per type: " + type);
                                }
                                mapperByClass.put(type, mapper);
                            }
                        }
                    }
                }
            }

        }
        return mapper;
    }

    @Nullable
    private DbBinder findBinderByType(@NotNull Class<?> parameterType, @Nullable Type genericType) {
        Class<?> effectiveType = getEffectiveType(parameterType, genericType);
        DbBinder binder = binderByClass.get(effectiveType);
        if (binder == null) {
            binder = findBinderByInterfaces(effectiveType.getInterfaces());
            if (binder == null) {
                Class superClass = effectiveType.getSuperclass();
                binder = superClass == null ? null : findBinderByType(superClass, null);
            }
        }
        return binder;
    }

    private static Class<?> getEffectiveType(@NotNull Class<?> parameterType, @Nullable Type genericType) {
        Class<?> effectiveType = parameterType;
        if (isBatchType(parameterType)) {
            if (genericType instanceof ParameterizedType) {
                ParameterizedType pt = (ParameterizedType) genericType;
                Type[] args = pt.getActualTypeArguments();
                if (args.length == 1 && args[0] instanceof Class) {
                    effectiveType = (Class) args[0];
                }
            } else if (genericType instanceof Class && ((Class) genericType).isArray()) {
                effectiveType = ((Class) genericType).getComponentType();
            }
        }
        return effectiveType;
    }

    private static boolean isBatchType(@NotNull Class<?> type) {
        return Iterable.class.isAssignableFrom(type) || Iterator.class.isAssignableFrom(type) || type.isArray();
    }

    @Nullable
    private static BatchIteratorFactory getBatchHandlerFactory(@NotNull Class<?> parameterType) {
        if (!isBatchType(parameterType)) {
            return null;
        }
        if (Iterable.class.isAssignableFrom(parameterType)) {
            return BatchIteratorFactory.ITERABLE_HANDLER;
        }
        if (Iterator.class.isAssignableFrom(parameterType)) {
            return BatchIteratorFactory.ITERATOR_HANDLER;
        }
        if (parameterType.isArray()) {
            return BatchIteratorFactory.ARRAY_HANDLER;
        }
        throw new IllegalStateException("No BatchIteratorFactory found for " + parameterType);
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

    static class OpRef {
        @NotNull
        final String parsedSql;

        @NotNull
        DbMapper resultMapper;

        @NotNull
        final Map<String, List<Integer>> parametersNamesMapping;

        @NotNull
        final BindInfo[] bindings;

        final boolean useGeneratedKeys;

        @Nullable
        final BatchIteratorFactory batchIteratorFactory;
        final int batchArgIdx;
        final int batchSize;

        private OpRef(@NotNull String parsedSql, @NotNull DbMapper resultMapper, boolean useGeneratedKeys,
                      @NotNull Map<String, List<Integer>> parametersNamesMapping, @NotNull BindInfo[] bindings,
                      @Nullable BatchIteratorFactory batchIteratorFactory, int batchArgIdx, int batchSize) {
            this.parsedSql = parsedSql;
            this.useGeneratedKeys = useGeneratedKeys;
            this.resultMapper = resultMapper;
            this.parametersNamesMapping = parametersNamesMapping;
            this.bindings = bindings;
            this.batchIteratorFactory = batchIteratorFactory;
            this.batchArgIdx = batchArgIdx;
            this.batchSize = batchSize;
        }
    }

    static class BindInfo {
        @NotNull
        final String mappedName;

        @NotNull
        final DbBinder binder;

        final int argumentIdx;

        @Nullable
        final Field field;

        @Nullable
        final Method getter;

        private BindInfo(@NotNull String mappedName, @NotNull DbBinder binder, int argumentIdx, @Nullable Field field, @Nullable Method getter) {
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

    void updateTimer(Method method, long t0) {
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
