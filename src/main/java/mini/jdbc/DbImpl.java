package mini.jdbc;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.sql.DataSource;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.sql.SQLException;
import java.util.Objects;

/**
 *
 */
public class DbImpl implements Db {

    private ThreadLocal<DbConnection> activeConnections = new ThreadLocal<>();

    @NotNull
    private DataSource dataSource;

    public DbImpl(@NotNull DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @NotNull
    public <T> T attachDbi(@NotNull final T impl, @NotNull Class<T> daoInterface) {
        Objects.requireNonNull(impl);
        Objects.requireNonNull(daoInterface);

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
        Objects.requireNonNull(op);
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
        Objects.requireNonNull(queryInterface);
        //TODO:
        throw new UnsupportedOperationException("noy yet");
    }

}
