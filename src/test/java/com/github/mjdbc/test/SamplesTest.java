package com.github.mjdbc.test;

import com.github.mjdbc.DbTimer;
import com.github.mjdbc.test.asset.dbi.SampleDbi;
import com.github.mjdbc.test.asset.dbi.SampleDbiImpl;
import com.github.mjdbc.test.asset.model.Gender;
import com.github.mjdbc.test.asset.model.User;
import com.github.mjdbc.test.asset.sql.UserSql;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

/**
 * Set of examples to demonstrate basic Db functionality.
 */
public class SamplesTest extends DbTest {
    /**
     * Data access interface. Set of complex SQL ops & business logic joined into transactions.
     */
    private SampleDbi dbi;

    /**
     * Set of raw SQL queries.
     */
    private UserSql sampleQueries;

    @Before
    public void setUp() {
        super.setUp();
        dbi = db.attachDbi(new SampleDbiImpl(db), SampleDbi.class);

        // Usually DB must be accessed by calling Dbi interface method.
        // But for testing we create a separate queries interface here.
        sampleQueries = db.attachSql(UserSql.class);
    }

    /**
     * Checks that database is not empty. Note that we can always use pure JDBC if want to work on very low level.
     */
    @Test
    public void checkDatabaseNotEmpty() {
        db.executeV(c -> {
            try (Statement statement = c.getConnection().createStatement()) {
                //noinspection SqlNoDataSourceInspection,SqlResolve
                try (ResultSet rs = statement.executeQuery("SELECT * FROM users")) {
                    assertTrue(rs.next());
                }
            }
        });
    }

    /**
     * Check that Dbi is initialized and is capable to run simple queries.
     */
    @Test
    public void checkDbi() {
        User user = dbi.getUserByLogin("u1");
        assertNotNull(user);
        assertEquals("u1", user.login);
    }

    /**
     * Check that raw sql interface is initialized and ready to use.
     */
    @Test
    public void checkQuery() {
        User user = sampleQueries.getUserByLogin("u1");
        assertNotNull(user);
        assertEquals("u1", user.login);
    }

    /**
     * Check some built-in mappers for primitive types.
     */
    @Test
    public void checkIntMapper() {
        int n = sampleQueries.countUsers();
        assertEquals(2, n);
    }

    /**
     * Check  built-in mapper for List type.
     */
    @Test
    public void checkListMapper() {
        List<User> users = sampleQueries.selectAllUsers();
        assertEquals(2, users.size());
    }

    /**
     * SampleDbi.updateScore() method internally uses 2 raw queries in the same transaction.
     */
    @Test
    public void checkMultipleQueriesSameTransaction() {
        long originalScore = 0; // default value for new database.
        long oldScore = dbi.updateScore("u1", 1);
        assertEquals(originalScore, oldScore);

        User user = dbi.getUserByLogin("u1");
        assertNotNull(user);
        assertEquals(1L, user.score);
    }

    /**
     * SampleDbi.updateScoreAndRollback() method updates user score and fails -> triggers rollback.
     */
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

            User updatedUser = sampleQueries.getUserByLogin("u1");
            assertNotNull(updatedUser);
            assertEquals(oldUser.score, updatedUser.score);
        }
    }

    /**
     * Let's update some field using beans and check that object fields were automatically mapped to query parameters.
     */
    @Test
    public void checkBeanBinder() {
        User originalUser = sampleQueries.getUserByLogin("u1");
        assertNotNull(originalUser);
        originalUser.score = originalUser.score + 1;
        sampleQueries.updateScore(originalUser);

        // fetch data and check it was updated
        User updatedUser = sampleQueries.getUserByLogin("u1");
        assertNotNull(updatedUser);
        assertEquals(originalUser.score, updatedUser.score);
    }

    /**
     * Test that bean insertion works and returns valid ids.
     */
    @Test
    public void checkInsert() {
        User u = new User();
        u.login = "u3";
        u.firstName = "First3";
        u.lastName = "Last3";
        u.gender = Gender.FEMALE;
        u.registrationDate = new Timestamp(System.currentTimeMillis());
        dbi.createUser(u);

        // check that valid id is assigned.
        assertNotNull(u.id);

        // check that user can be found by id provided.
        User checkUser1 = dbi.getUserById(u.id);
        assertNotNull(checkUser1);
        assertEquals(u.login, checkUser1.login);

        // check that user can be found by login.
        User checkUser2 = dbi.getUserByLogin(u.login);
        assertNotNull(checkUser2);
        assertEquals(u.id, checkUser2.id);
    }

    /**
     * Test that update returns number of records updated
     */
    @Test
    public void checkUpdateResults() {
        List<User> originalUsers = sampleQueries.selectAllUsers();
        assertTrue(originalUsers.size() > 0);

        int score = 100;
        int n = sampleQueries.updateAllScores(score);
        assertEquals(originalUsers.size(), n);

        List<User> updatedUsers = sampleQueries.selectAllUsers();
        for (User u : updatedUsers) {
            assertEquals("Score not matched for user: " + u.login, score, u.score);
        }
    }


    /**
     * Test that delete returns number of records updated
     */
    @Test
    public void checkDeleteResults() {
        List<User> originalUsers = sampleQueries.selectAllUsers();
        assertTrue(originalUsers.size() > 0);

        int n = sampleQueries.deleteAll();
        assertEquals(originalUsers.size(), n);

        List<User> newUsers = sampleQueries.selectAllUsers();
        assertTrue(newUsers.isEmpty());
    }


    /**
     * Check that statistics is collected for Dbi methods.
     */
    @Test
    public void checkTxTimer() throws NoSuchMethodException {
        dbi.getUserByLogin("u1");
        Method method = SampleDbi.class.getMethod("getUserByLogin", String.class);
        DbTimer timer = db.getTimers().get(method);
        assertNotNull(timer);
        assertTrue(timer.getInvocationCount() == 1);
        assertTrue(timer.getTotalTimeInNanos() > 0);
    }

    /**
     * Check that statistics is updated for @Sql methods.
     */
    @Test
    public void checkSqlTimer() throws NoSuchMethodException {
        sampleQueries.countUsers();
        Method method = UserSql.class.getMethod("countUsers");
        DbTimer timer = db.getTimers().get(method);
        assertNotNull(timer);
        assertTrue(timer.getInvocationCount() == 1);
        assertTrue(timer.getTotalTimeInNanos() > 0);
    }
}
