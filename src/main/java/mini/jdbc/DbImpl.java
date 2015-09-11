package mini.jdbc;

import org.jetbrains.annotations.NotNull;

import javax.sql.DataSource;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 */
public class DbImpl implements Db {

    private ThreadLocal<Connection> activeConnections = new ThreadLocal<>();

    @NotNull
    private DataSource dataSource;

    public DbImpl(@NotNull DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public <T> T attach(@NotNull final Dbi impl, Class<T> interfaceClass) {
        if (impl.getContext() != null) {
            throw new RuntimeException("todo");
        }
        InvocationHandler handler = new InvocationHandler() {
            public Object invoke(Object proxy, final Method method, final Object[] args) throws Throwable {
                return impl.getContext().execute(new DbOp<Void>() {
                    public Void run(@NotNull Connection c) throws Exception {
                        //todo: check method return params
                        method.invoke(impl, args);
                        return null;
                    }
                });
            }
        };

        if (!interfaceClass.isAssignableFrom(impl.getClass())) {
            throw new RuntimeException("todo");
        }
        //noinspection unchecked
        T result = (T) Proxy.newProxyInstance(impl.getClass().getClassLoader(), new Class[]{interfaceClass}, handler);
        impl.setContext(new DbContext(this));
        return result;
    }

    public <T> T execute(@NotNull DbOp<T> op) {
        return execute(op, new DbContext(this));
    }

    public <T> T execute(@NotNull DbOp<T> op, @NotNull DbContext context) {
        boolean topLevel = false;
        Connection c = activeConnections.get();
        try {
            try {
                if (c == null) {
                    c = dataSource.getConnection();
                    topLevel = true;
                }
                if (context.connection == null) {
                    context.connection = c;
                } else if (context.connection != c) {
                    throw new IllegalStateException("Context is in illegal state!");
                }

                T res = op.run(c);
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
                    context.connection = null;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
