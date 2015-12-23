package com.github.mjdbc.test;

import com.github.mjdbc.Db;
import com.github.mjdbc.DbImpl;
import com.github.mjdbc.test.asset.model.User;
import com.github.mjdbc.test.asset.model.UserId;
import com.github.mjdbc.test.asset.sql.InvalidMapperSql;
import com.github.mjdbc.test.asset.sql.UserSql;
import com.github.mjdbc.test.util.DbUtils;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * Tests for DbValue (Int,Long..) support.
 */
public class MapperLookupTest extends Assert {
    /**
     * Low level connection pool.
     */
    private HikariDataSource ds;

    private Db db;
    private UserSql sql;

    @Before
    public void setUp() {
        ds = DbUtils.prepareDataSource("sample");
        db = new DbImpl(ds);
        sql = db.attachSql(UserSql.class);
    }

    @After
    public void tearDown() {
        ds.close();
    }

    @Test
    public void checkUserRegistered() {
        User u = sql.getUserByLogin("u1");
        assertNotNull(u);
    }

    @Test
    public void checkUserIdRegistered() {
        List<UserId> ids = sql.selectAllUserIds();
        assertTrue(!ids.isEmpty());
    }

    @Test
    public void checkEmptyListIsReturned() {
        List<User> users = sql.selectAllUserByMinScore(1000);
        assertNotNull(users);
        assertTrue(users.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkInvalidMapperRegistration() {
        db.attachSql(InvalidMapperSql.class);
    }
}
