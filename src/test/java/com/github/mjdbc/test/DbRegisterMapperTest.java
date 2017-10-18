package com.github.mjdbc.test;

import com.github.mjdbc.Mappers;
import com.github.mjdbc.test.asset.model.User;
import com.github.mjdbc.test.asset.model.UserId;
import com.github.mjdbc.test.asset.sql.UserSql;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for Db::registerMapper method.
 */
public class DbRegisterMapperTest extends DbTest {
    @Before
    public void setUp() {
        super.setUp();
        db.registerMapper(UserId.class, UserId.MAPPER);
        db.registerMapper(User.class, User.MAPPER);
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
        db.registerMapper(null, Mappers.StringMapper);
    }

}
