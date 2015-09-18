package com.github.mjdbc.test;

import com.github.mjdbc.DbImpl;
import com.github.mjdbc.test.util.DbUtils;
import com.zaxxer.hikari.HikariDataSource;
import org.jetbrains.annotations.NotNull;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

public abstract class BaseJavaTypesTest<S> extends Assert {
    /**
     * Low level connection pool.
     */
    protected HikariDataSource ds;


    /**
     * Set of raw SQL queries.
     */
    protected S sql;

    @NotNull
    protected final Class<S> type;

    public BaseJavaTypesTest(@NotNull Class<S> type) {
        this.type = type;
    }

    @Before
    public void setUp() {
        ds = DbUtils.prepareDataSource("types");
        DbImpl db = new DbImpl(ds);
        sql = db.attachSql(type);
    }

    @After
    public void tearDown() {
        ds.close();
    }
}
