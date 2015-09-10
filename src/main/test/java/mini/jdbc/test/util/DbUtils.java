package mini.jdbc.test.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jetbrains.annotations.NotNull;

import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 */
public class DbUtils {
    static {
        try {
            DriverManager.registerDriver(new org.apache.derby.jdbc.EmbeddedDriver());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static HikariDataSource prepareDataSource(@NotNull String config) throws SQLException {
        return new HikariDataSource(new HikariConfig("/" + config));
    }
}
