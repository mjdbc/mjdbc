package com.github.mjdbc.test;

import com.github.mjdbc.DbImpl;
import com.github.mjdbc.test.asset.model.ABean;
import com.github.mjdbc.test.asset.model.MultipleMappersBean1;
import com.github.mjdbc.test.asset.model.User;
import com.github.mjdbc.test.asset.model.UserId;
import com.github.mjdbc.test.asset.sql.*;
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

    /**
     * Check that @Mapper annotation is supported
     */
    @Test
    public void checkMapperAnnotation() {
        ABean bean = db.attachSql(ValidBeansSql.class).selectABean();
        assertEquals(1, bean.value);
    }

    /**
     * Check that multiple mapper variants triggers error
     */
    @Test(expected = IllegalArgumentException.class)
    public void checkMultipleMappersTriggerError1() {
        db.attachSql(MultipleMappersBean1Sql.class);
    }

    /**
     * Check that multiple mapper variants triggers error
     */
    @Test(expected = IllegalArgumentException.class)
    public void checkMultipleMappersTriggerError2() {
        db.attachSql(MultipleMappersBean2Sql.class);
    }

    /**
     * Check that manual mapper registration does not trigger error for a bean with multiple mappers
     */
    @Test
    public void checkMultipleMappersTriggerError() {
        db.registerMapper(MultipleMappersBean1.class, MultipleMappersBean1.SOME_MAPPER1);
        MultipleMappersBean1 bean = db.attachSql(MultipleMappersBean1Sql.class).selectABean();
        assertEquals(1, bean.value);
    }
}
