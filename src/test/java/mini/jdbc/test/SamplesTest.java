package mini.jdbc.test;

import com.zaxxer.hikari.HikariDataSource;
import junit.framework.Assert;
import mini.jdbc.Db;
import mini.jdbc.DbImpl;
import mini.jdbc.test.asset.SampleQueries;
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
import java.util.List;

/**
 *
 */
public class SamplesTest extends org.junit.Assert {
    /**
     * Low level connection pool.
     */
    private HikariDataSource ds;

    /**
     * Custom database interface. DAO.
     */
    private SampleDbi dbi;

    /**
     * Set of queries.
     */
    private SampleQueries sampleQueries;

    @Before
    public void setUp() {
        ds = DbUtils.prepareDataSource("sample");
        DbImpl db = new DbImpl(ds);
        dbi = db.attachDbi(new SampleDbiImpl(db), SampleDbi.class);
        sampleQueries = db.attachQueries(SampleQueries.class);
    }

    @After
    public void tearDown() {
        dbi = null;
        ds.close();
    }

    @Test
    public void checkDatabaseNotEmpty() throws SQLException {
        Assert.assertNotNull(ds);
        Db db = new DbImpl(ds);
        db.executeV(c -> {
            try (Statement statement = c.sqlConnection.createStatement()) {
                try (ResultSet rs = statement.executeQuery("SELECT * FROM users")) {
                    assertTrue(rs.next());
                }
            }
        });
    }

    @Test
    public void checkDbi() throws SQLException {
        User user = dbi.getUserByLogin("u1");
        assertNotNull(user);
        assertEquals("u1", user.login);
    }

    @Test
    public void checkQuery() throws SQLException {
        User user = sampleQueries.selectUser("u1");
        assertNotNull(user);
        assertEquals("u1", user.login);
    }

    @Test
    public void checkIntMapper() throws SQLException {
        int n = sampleQueries.countUsers();
        assertEquals(2, n);
    }

    @Test
    public void checkListMapper() throws SQLException {
        List<User> users = sampleQueries.selectAllUsers();
        assertEquals(2, users.size());
    }

}
