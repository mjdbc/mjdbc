package com.github.mjdbc.test;

import com.github.mjdbc.DbConnection;
import com.github.mjdbc.DbPreparedStatement;
import com.github.mjdbc.Mappers;
import java.sql.Statement;
import java.util.HashMap;
import org.junit.Test;

/**
 * Common class for different DB tests.
 */
public class DbActiveConnectionTest extends DbTest {

    @Test(expected = IllegalStateException.class)
    public void checkNoActiveConnectionByDefault() {
        db.getActiveConnection();
    }

    @Test(expected = IllegalStateException.class)
    public void checkNoActiveConnectionAfterExecute() {
        db.execute(c -> null);
        db.getActiveConnection();
    }

    @Test
    public void checkActiveConnectionIsPresent() {
        db.executeV(c -> {
            DbConnection ac = db.getActiveConnection();
            assertNotNull(c);
            assertSame(c, ac);
        });
        db.executeNN(c -> {
            DbConnection ac = db.getActiveConnection();
            assertNotNull(c);
            assertSame(c, ac);
            return "";
        });

        db.execute(c -> {
            DbConnection ac = db.getActiveConnection();
            assertNotNull(c);
            assertSame(c, ac);
            return null;
        });
    }

    @Test
    public void checkCreateStatement() {
        db.executeV(c -> {
            DbConnection ac = db.getActiveConnection();
            Statement statement = ac.createStatement();
            assertNotNull(statement);
        });
    }

    @Test
    public void checkPrepareStatement1() {
        db.executeV(c -> {
            DbPreparedStatement ps = c.prepareStatement("SELECT id FROM users");
            assertSame(c, ps.getDbConnection());
        });
    }

    @Test
    public void checkPrepareStatement2() {
        db.executeV(c -> {
            DbPreparedStatement ps = c.prepareStatement("SELECT id FROM users", Integer.class);
            assertSame(c, ps.getDbConnection());
        });
    }

    @Test
    public void checkPrepareStatement3() {
        db.executeV(c -> {
            DbPreparedStatement ps = c.prepareStatement("SELECT id FROM users", Mappers.LongMapper);
            assertSame(c, ps.getDbConnection());
        });
    }

    @Test
    public void checkPrepareStatement4() {
        db.executeV(c -> {
            DbPreparedStatement ps = c.prepareStatement("SELECT id FROM users", Mappers.LongMapper, true);
            assertSame(c, ps.getDbConnection());
        });
    }

    @Test
    public void checkPrepareStatement5() {
        db.executeV(c -> {
            DbPreparedStatement ps = c.prepareStatement("SELECT id FROM users", Mappers.LongMapper, new HashMap<>(), true);
            assertSame(c, ps.getDbConnection());
        });
    }
}
