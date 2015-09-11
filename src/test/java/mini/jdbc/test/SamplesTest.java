package mini.jdbc.test;

import com.zaxxer.hikari.HikariDataSource;
import junit.framework.Assert;
import mini.jdbc.Db;
import mini.jdbc.DbImpl;
import mini.jdbc.DbOp;
import mini.jdbc.test.util.DbUtils;
import org.jetbrains.annotations.NotNull;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 */
public class SamplesTest extends org.junit.Assert {
    private HikariDataSource ds;

    @Before
    public void setUp() {
        ds = DbUtils.prepareDataSource("sample");
    }

    @After
    public void tearDown() {
        ds.close();
    }

    @Test
    public void test1() throws SQLException {
        Assert.assertNotNull(ds);
        Db db = new DbImpl(ds);
        db.execute(new DbOp<Void>() {
            public Void run(@NotNull Connection c) throws Exception {
                try (ResultSet rs = c.createStatement().executeQuery("SELECT * FROM users")) {
                    assertTrue(rs.next());
                }
                return null;
            }
        });
    }


}
