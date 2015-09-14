package mini.jdbc;

import mini.jdbc.mapper.ListMapper;
import mini.jdbc.mapper.VoidMapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.sql.DataSource;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Objects.requireNonNull;

/**
 *
 */
public class DbImpl implements Db {

    private ThreadLocal<DbConnection> activeConnections = new ThreadLocal<>();

    private final Map<Method, OpRef> opByMethod = new ConcurrentHashMap<>();

    private final Map<Class, DbMapper> mapperByClass = new ConcurrentHashMap<>(Mappers.BUILT_IN_MAPPERS);

    private final Map<Class, DbParameterBinder> binderByClass = new ConcurrentHashMap<>(Binders.BUILT_IN_BINDERS);

    @NotNull
    private DataSource dataSource;

    public DbImpl(@NotNull DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public <T> void registerMapper(@NotNull Class<T> mapperClass, @NotNull DbMapper<T> mapper) {
        requireNonNull(mapperClass);
        requireNonNull(mapper);
        mapperByClass.put(mapperClass, mapper);
    }

    @Override
    public void registerBinder(@NotNull Class binderClass, @NotNull DbParameterBinder binder) {
        requireNonNull(binderClass);
        requireNonNull(binder);
        binderByClass.put(binderClass, binder);
    }

    @NotNull
    public <T> T attachDbi(@NotNull final T impl, @NotNull Class<T> daoInterface) {
        requireNonNull(impl);
        requireNonNull(daoInterface);

        InvocationHandler handler = (proxy, method, args) -> {
            if (method.getAnnotation(Tx.class) != null) {
                //noinspection unchecked
                return execute(c -> (T) method.invoke(impl, args));
            } else {
                return method.invoke(impl, args);
            }
        };

        //noinspection unchecked
        return (T) Proxy.newProxyInstance(impl.getClass().getClassLoader(), new Class[]{daoInterface}, handler);
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

    @SuppressWarnings("unchecked")
    private <T> T executeImpl(@NotNull Object op) {
        requireNonNull(op);
        boolean topLevel = false;
        DbConnection c = activeConnections.get();
        try {
            try {
                if (c == null) {
                    c = new DbConnection(dataSource.getConnection());
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
                throw new RuntimeException(e);
            } finally {
                if (topLevel) {
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
    public <T> T attachQueries(@NotNull Class<T> queryInterface) {
        requireNonNull(queryInterface);
        for (Method m : queryInterface.getMethods()) {
            registerMethod(m);
        }
        InvocationHandler handler = (proxy, method, args) -> {
            OpRef p = opByMethod.get(method);
            return DbImpl.this.execute(c -> {
                //noinspection unchecked
                DbStatement s = new DbStatement(c, p.parsedSql, p.resultMapper, p.parametersMapping);
                for (int i = 0; i < args.length; i++) {
                    DbParameterBinder binder = p.parameterBinders[i];
                    //noinspection unchecked
                    binder.bind(s.statement, i, args[i]);
                }
                if (p.resultMapper != VoidMapper.INSTANCE) {
                    return s.query();
                }
                s.update();
                return null;
            });
        };

        //noinspection unchecked
        return (T) Proxy.newProxyInstance(queryInterface.getClassLoader(), new Class[]{queryInterface}, handler);
    }

    private void registerMethod(Method m) {
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
            throw new RuntimeException("Not supported query result type: " + returnType);
        }

        // sql named params
        Sql sqlAnnotation = m.getAnnotation(Sql.class);
        if (sqlAnnotation == null) {
            throw new RuntimeException("Method has no @Sql annotation: " + m);
        }
        String sql = sqlAnnotation.value();
        if (sql.isEmpty()) {
            throw new RuntimeException("Illegal SQL: '" + sql + "' in method: " + m);
        }
        Map<String, List<Integer>> parametersMapping = new HashMap<>();
        String parsedSql = DbStatement.parse(sql, parametersMapping);

        // parameter binders
        List<DbParameterBinder> binders = new ArrayList<>();
        Class<?>[] parameterTypes = m.getParameterTypes();
        for (int i = 0; i < m.getParameterCount(); i++) {
            Class<?> parameterType = parameterTypes[i];
            DbParameterBinder binder = binderByClass.get(parameterType);
            if (binder == null) {
                // try to find binder by superclass.
                binder = findBinderByParents(parameterType);
                if (binder == null) {
                    throw new IllegalArgumentException("Can't find parameter binder for: " + parameterType);
                }
            }
            binders.add(binder);
        }

        // construct result mapping
        opByMethod.put(m, new OpRef(parsedSql, resultMapper, parametersMapping, binders.toArray(new DbParameterBinder[binders.size()])));
    }

    @Nullable
    private DbParameterBinder findBinderByParents(Class<?> parameterType) {
        Class superClass = parameterType.getSuperclass();
        while (superClass != Object.class) {
            DbParameterBinder binder = binderByClass.get(parameterType);
            if (binder != null) {
                return binder;
            }
            superClass = superClass.getSuperclass();
        }
        return findBinderByInterfaces(parameterType.getInterfaces());
    }

    private DbParameterBinder findBinderByInterfaces(Class<?>[] interfaces) {
        for (Class interfaceClass : interfaces) {
            DbParameterBinder binder = binderByClass.get(interfaceClass);
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
        final DbMapper resultMapper;

        @NotNull
        final Map<String, List<Integer>> parametersMapping;

        @NotNull
        final DbParameterBinder[] parameterBinders;

        public OpRef(@NotNull String parsedSql, @NotNull DbMapper resultMapper,
                     @NotNull Map<String, List<Integer>> parametersMapping, @NotNull DbParameterBinder[] parameterBinders) {
            this.parsedSql = parsedSql;
            this.resultMapper = resultMapper;
            this.parametersMapping = parametersMapping;
            this.parameterBinders = parameterBinders;
        }
    }
}
