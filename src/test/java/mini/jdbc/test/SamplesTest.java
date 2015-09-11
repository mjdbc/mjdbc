package mini.jdbc.test;

import com.zaxxer.hikari.HikariDataSource;
import junit.framework.Assert;
import mini.jdbc.Db;
import mini.jdbc.DbImpl;
import mini.jdbc.DbOp;
import mini.jdbc.test.asset.dbi.SampleDbi;
import mini.jdbc.test.asset.dbi.SampleDbiImpl;
import mini.jdbc.test.asset.model.User;
import mini.jdbc.test.util.DbUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 */
public class SamplesTest extends org.junit.Assert {
    /**
     * Low level connection pool.
     */
    private HikariDataSource ds;
    private Db db;

    /**
     * Custom database interface. DAO.
     */
    private SampleDbi dbi;

    @Before
    public void setUp() {
        ds = DbUtils.prepareDataSource("sample");
        db = new DbImpl(ds);
        dbi = db.attachDbi(new SampleDbiImpl(db), SampleDbi.class);
    }

    @After
    public void tearDown() {
        dbi = null;
        db = null;
        ds.close();
    }

    @Test
    public void checkDatabaseNotEmpty() throws SQLException {
        Assert.assertNotNull(ds);
        Db db = new DbImpl(ds);
        db.execute((DbOp<Void>) c -> {
            try (Statement statement = c.sqlConnection.createStatement()) {
                try (ResultSet rs = statement.executeQuery("SELECT * FROM users")) {
                    assertTrue(rs.next());
                }
            }
            return null;
        });
    }


    @Test
    public void checkDbi() throws SQLException {
        User user = dbi.getUserByLogin("u1");
        assertNotNull(user);
        assertEquals("u1", user.login);
    }

}
