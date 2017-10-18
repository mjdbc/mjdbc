package com.github.mjdbc.test;

import com.github.mjdbc.Db;
import com.github.mjdbc.MJDBC;
import com.github.mjdbc.test.util.DbUtils;
import com.zaxxer.hikari.HikariDataSource;
import org.jetbrains.annotations.NotNull;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

public abstract class DbTest extends Assert {

    public String dbName;
    /**
     * Low level connection pool.
     */
    public HikariDataSource ds;

    /**
     * Database instance.
     */
    public Db db;


    public DbTest() {
        this("sample");
    }

    public DbTest(@NotNull String dbName) {
        this.dbName = dbName;
    }

    @Before
    public void setUp() {
        ds = DbUtils.prepareDataSource(dbName);
        db = MJDBC.newDb(ds);
    }

    @After
    public void tearDown() {
        ds.close();
    }


}
