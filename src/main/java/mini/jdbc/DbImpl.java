package mini.jdbc;

import mini.jdbc.mapper.IntegerMapper;
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

    private final Map<Class, DbMapper> mapperByClass = new ConcurrentHashMap<Class, DbMapper>() {{
        // no return type
        put(Void.TYPE, VoidMapper.INSTANCE);
        put(Void.class, VoidMapper.INSTANCE);

        // primitive types
        put(Integer.TYPE, IntegerMapper.INSTANCE);
        put(Integer.class, IntegerMapper.INSTANCE);
    }};

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
            return execute(p.op);
        };

        //noinspection unchecked
        return (T) Proxy.newProxyInstance(queryInterface.getClassLoader(), new Class[]{queryInterface}, handler);
    }

    private void registerMethod(Method m) {
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
        opByMethod.put(m, new OpRef(parsedSql, resultMapper, parametersMapping));
    }

    private class OpRef {
        @NotNull
        String parsedSql;
        @NotNull
        DbMapper resultMapper;
        @NotNull
        Map<String, List<Integer>> parametersMapping;
        DbOp op;

        public OpRef(@NotNull String parsedSql, @NotNull DbMapper resultMapper, @NotNull Map<String, List<Integer>> parametersMapping) {
            this.parsedSql = parsedSql;
            this.resultMapper = resultMapper;
            this.parametersMapping = parametersMapping;
            this.op = c -> {
                //noinspection unchecked
                DbStatement s = new DbStatement(c, parsedSql, resultMapper, parametersMapping);
                if (resultMapper != VoidMapper.INSTANCE) {
                    return s.query();
                }
                s.update();
                return null;
            };
        }
    }
}
