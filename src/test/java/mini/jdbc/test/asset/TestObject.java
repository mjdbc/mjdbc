package mini.jdbc.test.asset;

import mini.jdbc.Dbi;
import mini.jdbc.Tx;

/**
 */
public abstract class TestObject implements Dbi {
    @Tx
    public void foo() {
    }
}
