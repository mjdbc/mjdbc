package com.github.mjdbc.test;

import com.github.mjdbc.DbImpl;
import com.github.mjdbc.test.asset.ReaderSql;
import com.github.mjdbc.test.asset.UserSql;
import com.github.mjdbc.test.asset.model.User;
import com.github.mjdbc.test.asset.model.UserId;
import com.github.mjdbc.test.util.DbUtils;
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
     * Check that mapper type is validated during registration
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
}
