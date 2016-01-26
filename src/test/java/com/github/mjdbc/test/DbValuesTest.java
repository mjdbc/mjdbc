package com.github.mjdbc.test;

import com.github.mjdbc.Db;
import com.github.mjdbc.DbStatement;
import com.github.mjdbc.Mappers;
import com.github.mjdbc.test.util.DbUtils;
import com.github.mjdbc.type.DbInt;
import com.github.mjdbc.type.DbLong;
import com.github.mjdbc.type.DbString;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Timestamp;

/**
 * Tests for DbValue (Int,Long..) support.
 */
public class DbValuesTest extends Assert {
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
    public void checkDbInt() {
        String login = db.execute(c -> new DbStatement<>(c, "SELECT login FROM users WHERE id = :id", Mappers.StringMapper)
                .set("id", () -> 1)
                .query());
        assertEquals("u1", login);
    }

    @Test
    public void checkDbIntNull() {
        String login = db.execute(c -> new DbStatement<>(c, "SELECT login FROM users WHERE id = :id", Mappers.StringMapper)
                .set("id", (DbInt) null)
                .query());
        assertNull(login);
    }

    @Test
    public void checkDbLong() {
        db.executeV(c -> new DbStatement<>(c, "UPDATE users SET score = 10").update());
        int res = db.executeNN(c -> new DbStatement<>(c, "SELECT COUNT(*) FROM users WHERE score > :min_score", Mappers.IntegerMapper)
                .set("min_score", () -> 1L)
                .queryNN());
        assertEquals(2, res);
    }

    @Test
    public void checkDbLongNull() {
        String login = db.execute(c -> new DbStatement<>(c, "SELECT login FROM users WHERE score > :min_score", Mappers.StringMapper)
                .set("min_score", (DbLong) null)
                .query());
        assertNull(login);
    }

    @Test
    public void checkDbString() {
        int n = db.executeNN(c -> new DbStatement<>(c, "SELECT COUNT(*) FROM users WHERE login = :login", Mappers.IntegerMapper)
                .set("login", () -> "u1")
                .queryNN());
        assertEquals(1, n);
    }

    @Test
    public void checkDbStringNull() {
        int n = db.executeNN(c -> new DbStatement<>(c, "SELECT COUNT(*) FROM users WHERE login = :login", Mappers.IntegerMapper)
                .set("login", (DbString) null)
                .queryNN());
        assertEquals(0, n);
    }

    @Test
    public void checkDbTimestamp() {
        int n = db.executeNN(c -> new DbStatement<>(c, "SELECT COUNT(*) FROM users WHERE reg_date > :reg_date", Mappers.IntegerMapper)
                .set("reg_date", () -> new Timestamp(0))
                .queryNN());
        assertEquals(2, n);
    }
}
