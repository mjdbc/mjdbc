package com.github.mjdbc.test;

import com.github.mjdbc.DbImpl;
import com.github.mjdbc.test.asset.sql.EmptyQuerySql;
import com.github.mjdbc.test.asset.sql.EmptySql;
import com.github.mjdbc.test.asset.sql.MissedParameterSql;
import com.github.mjdbc.test.asset.sql.UnboundBeanParameterSql;
import com.github.mjdbc.test.asset.sql.UnboundParameterSql;
import com.github.mjdbc.test.asset.model.User;
import com.github.mjdbc.test.asset.model.UserId;
import com.github.mjdbc.test.util.DbUtils;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for Db::attachSql method.
 */
public class DbAttachSqlTest extends Assert {
    /**
     * Low level connection pool.
     */
    private HikariDataSource ds;

    /**
     * Database instance.
     */
    private DbImpl db;


    @Before
    public void setUp() {
        ds = DbUtils.prepareDataSource("sample");
        db = new DbImpl(ds);
        db.registerMapper(UserId.class, UserId.MAPPER);
        db.registerMapper(User.class, User.MAPPER);
    }

    @After
    public void tearDown() {
        ds.close();
    }


    /**
     * Check that empty Sql interface is OK.
     */
    @Test
    public void checkAttachEmptySql() {
        EmptySql sql = db.attachSql(EmptySql.class);
        assertNotNull(sql);
    }

    /**
     * Check that attach of class triggers IllegalArgumentException.
     */
    @Test(expected = IllegalArgumentException.class)
    public void checkAttachClassThrowsIllegalArgumentException() {
        db.attachSql(String.class);
    }

    /**
     * Check that empty Sql method triggers IllegalArgumentException.
     */
    @Test(expected = IllegalArgumentException.class)
    public void checkEmptySqlQueryThrowsIllegalArgumentException() {
        db.attachSql(EmptyQuerySql.class);
    }

    /**
     * Check that missed Sql parameter triggers IllegalArgumentException.
     */
    @Test(expected = IllegalArgumentException.class)
    public void checkMissedParameterSqlThrowsIllegalArgumentException() {
        db.attachSql(MissedParameterSql.class);
    }

    /**
     * Check that missed Sql bean parameter triggers IllegalArgumentException.
     */
    @Test(expected = IllegalArgumentException.class)
    public void checkMissedBeanParameterSqlThrowsIllegalArgumentException() {
        db.attachSql(MissedParameterSql.class);
    }

    /**
     * Check that unbound parameter type in Sql  triggers IllegalArgumentException.
     */
    @Test(expected = IllegalArgumentException.class)
    public void checkUnboundParameterSqlThrowsIllegalArgumentException() {
        db.attachSql(UnboundParameterSql.class);
    }

    /**
     * Check that unbound bean parameter type in Sql  triggers IllegalArgumentException.
     */
    @Test(expected = IllegalArgumentException.class)
    public void checkUnboundBeanParameterSqlThrowsIllegalArgumentException() {
        db.attachSql(UnboundBeanParameterSql.class);
    }
}
