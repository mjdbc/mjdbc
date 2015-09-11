package mini.jdbc.test.asset;

import mini.jdbc.DbInterface;
import mini.jdbc.Tx;

/**
 */
public abstract class TestObject implements DbInterface {
    @Tx
    public void foo() {
    }
}
