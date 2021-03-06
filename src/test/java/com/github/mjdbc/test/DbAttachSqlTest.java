package com.github.mjdbc.test;

import com.github.mjdbc.DbImpl;
import com.github.mjdbc.test.asset.model.BeanWithStaticFieldMapper;
import com.github.mjdbc.test.asset.model.ValidBean;
import com.github.mjdbc.test.asset.model.error.MultipleMappersBean;
import com.github.mjdbc.test.asset.sql.BeanWithStaticFieldMapperSql;
import com.github.mjdbc.test.asset.sql.EmptyQuerySql;
import com.github.mjdbc.test.asset.sql.EmptySql;
import com.github.mjdbc.test.asset.sql.ValidBeansSql;
import com.github.mjdbc.test.asset.sql.error.BeanWithNullMapperSql;
import com.github.mjdbc.test.asset.sql.error.DuplicateParametersSql;
import com.github.mjdbc.test.asset.sql.error.FakeGettersBeanSql;
import com.github.mjdbc.test.asset.sql.error.IllegalParametersSql1;
import com.github.mjdbc.test.asset.sql.error.IllegalParametersSql2;
import com.github.mjdbc.test.asset.sql.error.IllegalParametersSql3;
import com.github.mjdbc.test.asset.sql.error.MissedParameterSql;
import com.github.mjdbc.test.asset.sql.error.MultipleMappersBean1Sql;
import com.github.mjdbc.test.asset.sql.error.NonFinalMapperBeanSql;
import com.github.mjdbc.test.asset.sql.error.NonPublicMapperBeanSql;
import com.github.mjdbc.test.asset.sql.error.NonStaticMapperBeanSql;
import com.github.mjdbc.test.asset.sql.error.UnboundBeanParameterSql;
import com.github.mjdbc.test.asset.sql.error.UnboundParameterSql;
import com.github.mjdbc.test.asset.sql.error.WildcardParametrizedReturnTypeSql;
import org.junit.Test;

/**
 * Tests for Db::attachSql method.
 */
public class DbAttachSqlTest extends DbTest {
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
     * Check that manual mapper registration does not trigger error for a bean with multiple mappers
     */
    @Test
    public void multipleMappersThrowsException3() {
        db.registerMapper(MultipleMappersBean.class, MultipleMappersBean.SOME_MAPPER1);
        MultipleMappersBean bean = db.attachSql(MultipleMappersBean1Sql.class).selectABean();
        assertEquals(1, bean.value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void fakeGettersThrowException() {
        db.attachSql(FakeGettersBeanSql.class);
    }


    @Test
    public void checkBeanWithStaticFieldMapperByAnnotation() {
        DbImpl dbImpl = (DbImpl) this.db;
        assertNull(dbImpl.getRegisteredMapperByType(BeanWithStaticFieldMapper.class));
        this.db.attachSql(BeanWithStaticFieldMapperSql.class);
        assertNotNull(dbImpl.getRegisteredMapperByType(BeanWithStaticFieldMapper.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkBeanNonPublicMapper() {
        db.attachSql(NonPublicMapperBeanSql.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkBeanNonStaticMapper() {
        db.attachSql(NonStaticMapperBeanSql.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkBeanNonFinalMapper() {
        db.attachSql(NonFinalMapperBeanSql.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkBeanNullMapper() {
        db.attachSql(BeanWithNullMapperSql.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkWildcardParametrizedReturnTypeThrowsException() {
        db.attachSql(WildcardParametrizedReturnTypeSql.class);
    }


}
