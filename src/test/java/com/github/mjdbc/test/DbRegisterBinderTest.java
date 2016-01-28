package com.github.mjdbc.test;

import com.github.mjdbc.Db;
import com.github.mjdbc.test.asset.model.DbType1;
import com.github.mjdbc.test.asset.model.DbType2;
import com.github.mjdbc.test.asset.model.User;
import com.github.mjdbc.test.asset.model.UserId;
import com.github.mjdbc.test.asset.sql.AmbiguousTypeSql;
import com.github.mjdbc.test.asset.sql.ReaderSql;
import com.github.mjdbc.test.asset.sql.UserSql;
import com.github.mjdbc.test.util.DbUtils;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.Reader;
import java.io.StringReader;
import java.sql.PreparedStatement;

/**
 * Tests for Db::registerBinder method.
 */
public class DbRegisterBinderTest extends Assert {
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
        //noinspection ConstantConditions,RedundantCast
        db.registerBinder((Class<Reader>) null, PreparedStatement::setCharacterStream);
    }

    @Test(expected = NullPointerException.class)
    public void checkNullBinderFunctionTriggersNullPointerException() {
        //noinspection ConstantConditions
        db.registerBinder(Reader.class, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void multipleBindersForSameObjectThrowException() {
        db.registerBinder(DbType1.class, (statement, idx, value) -> {
        });
        db.registerBinder(DbType2.class, (statement, idx, value) -> {
        });
        db.attachSql(AmbiguousTypeSql.class);
    }

}
