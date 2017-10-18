package com.github.mjdbc.test;

import com.github.mjdbc.test.asset.model.User;
import com.github.mjdbc.test.asset.model.UserId;
import com.github.mjdbc.test.asset.sql.UserSql;
import com.github.mjdbc.test.asset.sql.error.InvalidMapperSql;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for DbValue (Int,Long..) support.
 */
public class MapperLookupTest extends DbTest {
    private UserSql sql;

    @Before
    public void setUp() {
        super.setUp();
        sql = db.attachSql(UserSql.class);
    }

    @Test
    public void checkUserRegistered() {
        User u = sql.getUserByLogin("u1");
        assertNotNull(u);
    }

    @Test
    public void checkUserIdRegistered() {
        List<UserId> ids = sql.selectAllUserIds();
        assertTrue(!ids.isEmpty());
    }

    @Test
    public void checkEmptyListIsReturned() {
        List<User> users = sql.selectAllUserByMinScore(1000);
        assertNotNull(users);
        assertTrue(users.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkInvalidMapperRegistration() {
        db.attachSql(InvalidMapperSql.class);
    }
}
