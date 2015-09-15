package mini.jdbc.test;

import com.zaxxer.hikari.HikariDataSource;
import junit.framework.Assert;
import mini.jdbc.Db;
import mini.jdbc.DbImpl;
import mini.jdbc.DbTimer;
import mini.jdbc.test.asset.SampleQueries;
import mini.jdbc.test.asset.dbi.SampleDbi;
import mini.jdbc.test.asset.dbi.SampleDbiImpl;
import mini.jdbc.test.asset.model.Gender;
import mini.jdbc.test.asset.model.User;
import mini.jdbc.test.util.DbUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;
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
     * Database instance.
     */
    private DbImpl db;

    /**
     * Custom data access interface.
     */
    private SampleDbi dbi;

    /**
     * Set of queries.
     */
    private SampleQueries sampleQueries;

    @Before
    public void setUp() {
        ds = DbUtils.prepareDataSource("sample");
        db = new DbImpl(ds);
        dbi = db.attachDbi(new SampleDbiImpl(db), SampleDbi.class);

        // Usually DB must be accessed by calling dbi interface method.
        // But for testing we create a separate queries interface here.
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
        User user = sampleQueries.getUserByLogin("u1");
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
        User user = sampleQueries.getUserByLogin("u1");
        assertNotNull(user);
        int oldScore = dbi.updateScore(user.login, user.score + 1);
        assertEquals(user.score, oldScore);
    }

    @Test
    public void checkRollback() {
        User oldUser = sampleQueries.getUserByLogin("u1");
        assertNotNull(oldUser);
        try {
            dbi.updateScoreAndRollback(oldUser.login, 10);
            fail("Unreachable!");
        } catch (Exception e) {
            assertEquals(RuntimeException.class, e.getClass());
            assertEquals("Rollback!", e.getMessage());

            User newUser = sampleQueries.getUserByLogin("u1");
            assertNotNull(newUser);
            assertEquals(oldUser.score, newUser.score);
        }
    }

    @Test
    public void checkBeanBinder() {
        // executeUpdate score using bean binder
        User oldUser = sampleQueries.getUserByLogin("u1");
        assertNotNull(oldUser);
        oldUser.score = oldUser.score + 1;
        sampleQueries.updateScore(oldUser);

        // fetch data and check it was updated
        User newUser = sampleQueries.getUserByLogin("u1");
        assertNotNull(newUser);
        assertEquals(oldUser.score, newUser.score);
    }

    @Test
    public void checkInsert() {
        User u = new User();
        u.login = "u3";
        u.firstName = "First3";
        u.lastName = "Last3";
        u.gender = Gender.FEMALE;
        dbi.createUser(u);

        assertNotNull(u.id);

        User checkUser1 = dbi.getUserById(u.id);
        assertNotNull(checkUser1);
        assertEquals(u.login, checkUser1.login);

        User checkUser2 = dbi.getUserByLogin(u.login);
        assertNotNull(checkUser2);
        assertEquals(u.id, checkUser2.id);
    }

    @Test
    public void checkTxTimer() throws NoSuchMethodException {
        dbi.getUserByLogin("u1");
        Method method = SampleDbi.class.getMethod("getUserByLogin", String.class);
        DbTimer timer = db.getTimers().get(method);
        assertNotNull(timer);
        assertTrue(timer.getInvocationCount() > 0);
        assertTrue(timer.getTotalTimeInNanos() > 0);
    }

    @Test
    public void checkSqlTimer() throws NoSuchMethodException {
        sampleQueries.countUsers();
        Method method = SampleQueries.class.getMethod("countUsers");
        DbTimer timer = db.getTimers().get(method);
        assertNotNull(timer);
        assertTrue(timer.getInvocationCount() > 0);
        assertTrue(timer.getTotalTimeInNanos() > 0);
    }
}
