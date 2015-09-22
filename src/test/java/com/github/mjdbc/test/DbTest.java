package com.github.mjdbc.test;

import com.github.mjdbc.DbImpl;
import com.github.mjdbc.test.asset.EmptyQuerySql;
import com.github.mjdbc.test.asset.EmptySql;
import com.github.mjdbc.test.asset.MissedParameterSql;
import com.github.mjdbc.test.asset.ReaderSql;
import com.github.mjdbc.test.asset.UserSql;
import com.github.mjdbc.test.asset.model.User;
import com.github.mjdbc.test.asset.model.UserId;
import com.github.mjdbc.test.util.DbUtils;
import com.github.mjdbc.util.JavaType;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.Reader;
import java.io.StringReader;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

/**
 * Db interface tests
 */
public class DbTest extends Assert {
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
     * Check new mapper registration
     */
    @Test
    public void checkMapperOverride() {
        UserSql q = db.attachSql(UserSql.class);
        assertEquals(2, q.countUsers());

        db.registerMapper(Integer.class, r -> -r.getInt(1));
        assertEquals(-2, q.countUsers());
    }

    /**
     * Check that collection mappers override are not supported.
     */
    @Test(expected = IllegalArgumentException.class)
    public void checkCollectionMapperOverrideIsNotAllowed() {
        db.registerMapper(List.class, r -> new ArrayList());
        fail();
    }

    /**
     * Check that registration of null mapper triggers null pointer exception.
     */
    @Test(expected = NullPointerException.class)
    public void checkNullMapperThrowsNullPointerException() {
        //noinspection ConstantConditions
        db.registerMapper(String.class, null);
    }

    /**
     * Check that registration of mapped class = null triggers null pointer exception.
     */
    @Test(expected = NullPointerException.class)
    public void checkNullMappedClassThrowsNullPointerException() {
        //noinspection ConstantConditions
        db.registerMapper(null, JavaType.String.mapper);
    }


    /**
     * Check that new binder class can be registered and used.
     */
    @Test
    public void checkBinderRegistration() {
        db.registerBinder(Reader.class, PreparedStatement::setCharacterStream);
        ReaderSql q1 = db.attachSql(ReaderSql.class);
        q1.updateFirstNameWithReader("u1", new StringReader("x"));

        UserSql q2 = db.attachSql(UserSql.class);
        User u = q2.getUserByLogin("u1");
        assertNotNull(u);
        assertEquals("x", u.firstName);
    }

    /**
     * Check that registration of null binder triggers IllegalArgumentException
     */
    @Test(expected = NullPointerException.class)
    public void checkNullBinderTriggersNullPointerException() {
        //noinspection ConstantConditions
        db.registerBinder((Class<Reader>) null, PreparedStatement::setCharacterStream);
    }

    @Test(expected = NullPointerException.class)
    public void checkNullBinderFunctionTriggersNullPointerException() {
        //noinspection ConstantConditions
        db.registerBinder(Reader.class, null);
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
     * Check that empty Sql method triggers IllegalArgumentException.
     */
    @Test(expected = IllegalArgumentException.class)
    public void checkMissedParameterSqlThrowsIllegalArgumentException() {
        db.attachSql(MissedParameterSql.class);
    }

    /**
     * Check that empty Sql method triggers IllegalArgumentException.
     */
    @Test(expected = IllegalArgumentException.class)
    public void checkMissedBeanParameterSqlThrowsIllegalArgumentException() {
        db.attachSql(MissedParameterSql.class);
    }
}
