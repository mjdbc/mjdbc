package mini.jdbc.test.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

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

    public static HikariDataSource prepareDataSource(String config) {
        try {
            HikariDataSource dataSource = new HikariDataSource(new HikariConfig("/" + config + ".properties"));
            runScript("sql/" + config + "-drop.sql", dataSource, false);
            runScript("sql/" + config + "-create.sql", dataSource, true);
            return dataSource;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void runScript(String scriptFile, DataSource dataSource, boolean stopOnErrors) throws SQLException, IOException {
        try (Connection con = dataSource.getConnection()) {
            try (Reader reader = new InputStreamReader(DbUtils.class.getResourceAsStream("/" + scriptFile))) {
                ScriptRunner runner = new ScriptRunner(con, true, stopOnErrors);
                runner.setLogWriter(null);
                runner.setErrorLogWriter(null);
                runner.runScript(reader);
            }
        }
    }

}
