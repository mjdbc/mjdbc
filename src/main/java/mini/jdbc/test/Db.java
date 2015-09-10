package mini.jdbc.test;

import org.jetbrains.annotations.NotNull;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 */
public abstract class Db {

    private DataSource dataSource;

    private final ThreadLocal<Connection> activeConnections = new ThreadLocal<>();

    public Db() {
    }

    public <T> T execute(@NotNull DbOp<T> op) {
        Connection c = activeConnections.get();
        boolean topLevel = false;
        try {
            try {
                if (c == null) {
                    c = dataSource.getConnection();
                    activeConnections.set(c);
                    topLevel = true;
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
                    activeConnections.remove();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
