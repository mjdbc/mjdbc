package com.github.mjdbc.test;

import com.github.mjdbc.Db;
import com.github.mjdbc.test.asset.dbi.EmptyDbi;
import com.github.mjdbc.test.asset.dbi.EmptyDbiImpl;
import com.github.mjdbc.test.asset.dbi.ProfiledDbi;
import com.github.mjdbc.test.asset.dbi.ProfiledDbiImpl;
import com.github.mjdbc.test.asset.model.User;
import com.github.mjdbc.test.asset.model.UserId;
import com.github.mjdbc.test.util.DbUtils;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for Db::attachDbi method.
 */
public class DbAttachDbiTest extends Assert {
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

    @Test
    public void checkAttachEmptyDbi() {
        EmptyDbi dbi = db.attachDbi(new EmptyDbiImpl(), EmptyDbi.class);
        assertNotNull(dbi);
    }


    @Test(expected = NullPointerException.class)
    public void checkAttachNullDbiImplThrowsNullPointerException() {
        //noinspection ConstantConditions
        db.attachDbi(null, EmptyDbi.class);
    }

    @Test(expected = NullPointerException.class)
    public void checkAttachNullDbiInterfaceThrowsNullPointerException() {
        //noinspection ConstantConditions
        db.attachDbi(new EmptyDbiImpl(), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkAttachWrongDbiInterfaceThrowsIllegalArgumentException() {
        //noinspection ConstantConditions
        db.attachDbi(new EmptyDbiImpl(), Object.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkAttachWrongDbiImplementationThrowsIllegalArgumentException() {
        //noinspection ConstantConditions
        db.attachDbi(new Object(), Object.class);
    }

    @Test
    public void checkAttachedMethodsAreCalled() {
        ProfiledDbiImpl impl = new ProfiledDbiImpl();
        ProfiledDbi dbi = db.attachDbi(impl, ProfiledDbi.class);
        assertEquals(0, impl.counter1);
        assertEquals(0, impl.counter2);

        dbi.counter1();
        assertEquals(1, impl.counter1);
        assertEquals(0, impl.counter2);

        dbi.counter2();
        assertEquals(1, impl.counter1);
        assertEquals(1, impl.counter2);
    }

}
