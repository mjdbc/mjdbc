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
    public void checkDatabaseNotEmpty() {
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
    public void checkDbi() {
        User user = dbi.getUserByLogin("u1");
        assertNotNull(user);
        assertEquals("u1", user.login);
    }

    @Test
    public void checkQuery() {
        User user = sampleQueries.selectUser("u1");
        assertNotNull(user);
        assertEquals("u1", user.login);
    }

    @Test
    public void checkIntMapper() {
        int n = sampleQueries.countUsers();
        assertEquals(2, n);
    }

    @Test
    public void checkListMapper() {
        List<User> users = sampleQueries.selectAllUsers();
        assertEquals(2, users.size());
    }

    @Test
    public void checkMultipleQueriesSameTransaction() {
        User user = sampleQueries.selectUser("u1");
        assertNotNull(user);
        int oldScore = dbi.updateScore(user.login, 10);
        assertEquals(user.score, oldScore);
    }

    @Test
    public void checkRollback() {
        User oldUser = sampleQueries.selectUser("u1");
        assertNotNull(oldUser);
        try {
            dbi.updateScoreAndRollback(oldUser.login, 10);
            fail("Unreachable!");
        } catch (Exception e) {
            assertEquals(RuntimeException.class, e.getClass());
            assertEquals("Rollback!", e.getMessage());

            User newUser = sampleQueries.selectUser("u1");
            assertNotNull(newUser);
            assertEquals(oldUser.score, newUser.score);
        }
    }

}
