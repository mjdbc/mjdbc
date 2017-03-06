package com.github.mjdbc.test;

import com.github.mjdbc.Db;
import com.github.mjdbc.DbConnection;
import com.github.mjdbc.DbPreparedStatement;
import com.github.mjdbc.test.asset.model.GetterBean;
import com.github.mjdbc.test.asset.model.User;
import com.github.mjdbc.test.util.DbUtils;
import com.zaxxer.hikari.HikariDataSource;
import org.jetbrains.annotations.NotNull;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Tests for DbPreparedStatement calls.
 */
public class DbPreparedStatementTest extends Assert {
    /**
     * Low level connection pool.
     */
    private HikariDataSource ds;

    /**
     * Database instance.
     */
    private Db db;

    @Before
    public void setUp() {
        ds = DbUtils.prepareDataSource("sample");
        db = Db.newInstance(ds);
    }

    @After
    public void tearDown() {
        ds.close();
    }

    @Test
    public void checkBindBean() {
        db.executeV(c -> {
            User originalUser = getUserUnderTest(c);
            Assert.assertNotNull(originalUser);

            User updatedUser = shuffleAllFields(originalUser);
            Assert.assertNotEquals(originalUser, updatedUser);

            DbPreparedStatement stmt = new DbPreparedStatement<>(c, "UPDATE users SET "
                    + "login = :login, "
                    + "first_name = :firstName, "
                    + "last_name = :lastName, "
                    + "gender = :gender, "
                    + "score = :score, "
                    + "reg_date = :registrationDate "
                    + "WHERE id = :id");

            stmt.bindBean(db, updatedUser, false);
            stmt.update();

            User userAfterUpdate = getUserUnderTest(c);
            Assert.assertEquals(updatedUser, userAfterUpdate);
        });
    }

    @Test
    public void checkBindBeanAndCustomFields() {
        db.executeV(c -> {
            User originalUser = getUserUnderTest(c);
            User updatedUser = shuffleAllFields(originalUser);

            DbPreparedStatement stmt = new DbPreparedStatement<>(c, "UPDATE users SET "
                    + "login = :login, "
                    + "first_name = :customField, "
                    + "last_name = :lastName, "
                    + "gender = :gender, "
                    + "score = :score, "
                    + "reg_date = :registrationDate "
                    + "WHERE id = :id");

            stmt.bindBean(db, updatedUser);
            updatedUser.firstName = updatedUser.firstName + "_custom";
            stmt.set("customField", updatedUser.firstName);
            stmt.update();

            User userAfterUpdate = getUserUnderTest(c);
            Assert.assertEquals(updatedUser, userAfterUpdate);
        });
    }

    @Test
    public void checkBindBeanChecksCustomFields() {
        db.executeV(c -> {
            User originalUser = getUserUnderTest(c);
            DbPreparedStatement stmt = new DbPreparedStatement<>(c, "UPDATE users SET "
                    + "first_name = :customField "
                    + "WHERE id = :id");

            try {
                stmt.bindBean(db, originalUser, false);
                fail("Custom fields must not be allowed!");
            } catch (Exception ignored) {
            }
        });
    }

    @Test
    public void checkBindBeanExceptionOnFieldAccess() {
        String exceptionMessage = "*it happens";
        db.executeV(c -> {
            try {
                DbPreparedStatement stmt = new DbPreparedStatement<>(c, "SELECT * FROM users  WHERE id = :id ", User.MAPPER);
                stmt.bindBean(db, new BadGetterBean(exceptionMessage));
                fail("Expected SQLException!");
            } catch (SQLException e) {
                Assert.assertNotNull(e.getCause());
                Assert.assertEquals(InvocationTargetException.class, e.getCause().getClass());
                Assert.assertNotNull(e.getCause().getCause());
                Assert.assertEquals(RuntimeException.class, e.getCause().getCause().getClass());
                Assert.assertEquals(exceptionMessage, e.getCause().getCause().getMessage());
            }
        });
    }

    @NotNull
    private static User shuffleAllFields(@NotNull User user) {
        User res = new User();
        res.id = user.id;
        res.login = user.login + "updated";
        res.firstName = user.firstName + "updated";
        res.lastName = user.lastName + "updated";
        res.gender = user.gender.opposite();
        res.score = user.score + 100;
        res.registrationDate = new Timestamp(user.registrationDate.toInstant().toEpochMilli() + 1000);
        return res;
    }

    private User getUserUnderTest(@NotNull DbConnection c) throws java.sql.SQLException {
        return new DbPreparedStatement<>(c, "SELECT * FROM users WHERE id = 1", User.MAPPER).query();
    }

    public static class BadGetterBean extends GetterBean {
        @NotNull
        private final String exceptionMessage;

        public BadGetterBean(@NotNull String exceptionMessage) {
            this.exceptionMessage = exceptionMessage;
        }

        @Override
        public long getId() {
            throw new RuntimeException(exceptionMessage);
        }
    }
}
