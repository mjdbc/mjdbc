package com.github.mjdbc.test;

import com.github.mjdbc.DbConnection;
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
}
