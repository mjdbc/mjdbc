package mini.jdbc.test.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jetbrains.annotations.NotNull;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Connection;
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

    public static HikariDataSource prepareDataSource(@NotNull String config, String dbFile) {
        try {
            HikariDataSource dataSource = new HikariDataSource(new HikariConfig("/" + config));
            dropDb("sql/" + dbFile + "-drop.sql", dataSource);
            createDb("sql/" + dbFile + "-create.sql", dataSource);
            return dataSource;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void createDb(String scriptFile, DataSource dataSource) throws SQLException, IOException {
        // Create MySql Connection
        try (Connection con = dataSource.getConnection()) {
            try (Reader reader = new InputStreamReader(DbUtils.class.getResourceAsStream("/" + scriptFile))) {
                runScript(con, reader, true);
            }

        }
    }

    public static void dropDb(String scriptFile, DataSource dataSource) throws SQLException, IOException {
        // Create MySql Connection
        try (Connection con = dataSource.getConnection()) {
            try (Reader reader = new InputStreamReader(DbUtils.class.getResourceAsStream("/" + scriptFile))) {
                runScript(con, reader, false);
            }
        }
    }


    private static void runScript(Connection con, Reader reader, boolean stopOnErrors) throws IOException, SQLException {
        ScriptRunner runner = new ScriptRunner(con, true, stopOnErrors);
        runner.setLogWriter(null);
        runner.setErrorLogWriter(null);
        runner.runScript(reader);
    }

}
