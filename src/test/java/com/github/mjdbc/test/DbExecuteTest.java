package com.github.mjdbc.test;

import com.github.mjdbc.Db;
import com.github.mjdbc.DbStatement;
import com.github.mjdbc.Mappers;
import com.github.mjdbc.test.util.DbUtils;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for Db::execute method.
 */
public class DbExecuteTest extends Assert {
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
    public void checkExecute() {
        Integer n = db.execute(c -> new DbStatement<>(c, "SELECT COUNT(*) FROM users", Mappers.IntegerMapper).query());
        assertEquals(new Integer(2), n);
    }
}
