package com.github.mjdbc.test;

import com.github.mjdbc.DbPreparedStatement;
import com.github.mjdbc.Mappers;
import com.github.mjdbc.test.asset.model.User;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

/**
 * Tests for Db::execute method.
 */
public class DbExecuteTest extends DbTest {

    @Test
    public void checkExecute() {
        Integer n = db.execute(c -> new DbPreparedStatement<>(c, "SELECT COUNT(*) FROM users", Mappers.IntegerMapper).query());
        assertEquals(new Integer(2), n);
    }


    @Test
    public void checkExecuteWithManualMapper() {
        User u = db.execute(c -> new DbPreparedStatement<>(c, "SELECT * FROM users", User.MAPPER).query());
        assertNotNull(u);
    }

    @Test
    public void checkExecuteWithAutoMapperFromClass() {
        User u = db.execute(c -> new DbPreparedStatement<>(c, "SELECT * FROM users", User.class).query());
        assertNotNull(u);
    }

    @Test
    public void checkExecuteWithManualMapperAndCollection() {
        List<User> users = db.executeNN(c -> new DbPreparedStatement<>(c, "SELECT * FROM users", User.MAPPER).queryList());
        checkAllUsersList(users);
    }

    private void checkAllUsersList(@NotNull List<User> users) {
        assertNotNull(users);
        assertEquals(2, users.size());
        assertNotNull(users.get(0));
        assertNotNull(users.get(1));
    }

    @Test
    public void checkExecuteWithMapperFromClassAndCollection() {
        List<User> users = db.executeNN(c -> new DbPreparedStatement<>(c, "SELECT * FROM users", User.class).queryList());
        checkAllUsersList(users);
    }
}
