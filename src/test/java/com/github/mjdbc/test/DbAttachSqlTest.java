package com.github.mjdbc.test;

import com.github.mjdbc.Db;
import com.github.mjdbc.test.asset.model.User;
import com.github.mjdbc.test.asset.model.UserId;
import com.github.mjdbc.test.asset.model.ValidBean;
import com.github.mjdbc.test.asset.model.error.MultipleMappersBean1;
import com.github.mjdbc.test.asset.sql.EmptyQuerySql;
import com.github.mjdbc.test.asset.sql.EmptySql;
import com.github.mjdbc.test.asset.sql.ValidBeansSql;
import com.github.mjdbc.test.asset.sql.error.DuplicateParametersSql;
import com.github.mjdbc.test.asset.sql.error.FakeGettersBeanSql;
import com.github.mjdbc.test.asset.sql.error.IllegalParametersSql1;
import com.github.mjdbc.test.asset.sql.error.IllegalParametersSql2;
import com.github.mjdbc.test.asset.sql.error.IllegalParametersSql3;
import com.github.mjdbc.test.asset.sql.error.MissedParameterSql;
import com.github.mjdbc.test.asset.sql.error.MultipleMappersBean1Sql;
import com.github.mjdbc.test.asset.sql.error.MultipleMappersBean2Sql;
import com.github.mjdbc.test.asset.sql.error.UnboundBeanParameterSql;
import com.github.mjdbc.test.asset.sql.error.UnboundParameterSql;
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
    private Db db;


    @Before
    public void setUp() {
        ds = DbUtils.prepareDataSource("sample");
        db = Db.newInstance(ds);
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
    public void sqlInterfaceWithNoMethodsIsOK() {
        EmptySql sql = db.attachSql(EmptySql.class);
        assertNotNull(sql);
    }

    /**
     * Check that attach of class triggers IllegalArgumentException.
     */
    @Test(expected = IllegalArgumentException.class)
    public void attachClassInstanceThrowsException() {
        db.attachSql(String.class);
    }

    /**
     * Check that empty Sql method triggers IllegalArgumentException.
     */
    @Test(expected = IllegalArgumentException.class)
    public void emptySqlQueryThrowsException() {
        db.attachSql(EmptyQuerySql.class);
    }

    /**
     * Check that missed Sql parameter triggers IllegalArgumentException.
     */
    @Test(expected = IllegalArgumentException.class)
    public void missedParameterThrowsException() {
        db.attachSql(MissedParameterSql.class);
    }

    /**
     * Check that missed Sql bean parameter triggers IllegalArgumentException.
     */
    @Test(expected = IllegalArgumentException.class)
    public void missedBeanParameterThrowsException() {
        db.attachSql(MissedParameterSql.class);
    }

    /**
     * Check that unbound parameter type in Sql  triggers IllegalArgumentException.
     */
    @Test(expected = IllegalArgumentException.class)
    public void unboundParameterThrowsException() {
        db.attachSql(UnboundParameterSql.class);
    }

    /**
     * Check that unbound bean parameter type in Sql  triggers IllegalArgumentException.
     */
    @Test(expected = IllegalArgumentException.class)
    public void unboundBeanParameterThrowsException() {
        db.attachSql(UnboundBeanParameterSql.class);
    }

    /**
     * Check that duplicate parameter names in @Bind triggers IllegalArgumentException.
     */
    @Test(expected = IllegalArgumentException.class)
    public void duplicateParameterNamesThrowsException() {
        db.attachSql(DuplicateParametersSql.class);
    }

    /**
     * Check that parameter names are checked to be valid Java identifiers
     */
    @Test
    public void illegalNamesThrowsException() {
        try {
            db.attachSql(IllegalParametersSql1.class);
            fail();
        } catch (IllegalArgumentException ignored) {
        }
        try {
            db.attachSql(IllegalParametersSql2.class);
            fail();
        } catch (IllegalArgumentException ignored) {
        }
        try {
            db.attachSql(IllegalParametersSql3.class);
            fail();
        } catch (IllegalArgumentException ignored) {
        }
    }

    /**
     * Check that @Mapper annotation is supported
     */
    @Test
    public void checkMapperAnnotation() {
        ValidBean bean = db.attachSql(ValidBeansSql.class).selectABean();
        assertEquals(1, bean.value);
    }

    /**
     * Check that multiple mapper variants triggers error
     */
    @Test(expected = IllegalArgumentException.class)
    public void multipleMappersThrowsException1() {
        db.attachSql(MultipleMappersBean1Sql.class);
    }

    /**
     * Check that multiple mapper variants triggers error
     */
    @Test(expected = IllegalArgumentException.class)
    public void multipleMappersThrowsException2() {
        db.attachSql(MultipleMappersBean2Sql.class);
    }

    /**
     * Check that manual mapper registration does not trigger error for a bean with multiple mappers
     */
    @Test
    public void multipleMappersThrowsException3() {
        db.registerMapper(MultipleMappersBean1.class, MultipleMappersBean1.SOME_MAPPER1);
        MultipleMappersBean1 bean = db.attachSql(MultipleMappersBean1Sql.class).selectABean();
        assertEquals(1, bean.value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void fakeGettersThrowException() {
        db.attachSql(FakeGettersBeanSql.class);
    }
}
