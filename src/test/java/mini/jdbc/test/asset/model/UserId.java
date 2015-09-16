package mini.jdbc.test.asset.model;

import mini.jdbc.DbMapper;
import mini.jdbc.type.impl.AbstractDbInt;

/**
 * Type safe id for User objects
 */
public final class UserId extends AbstractDbInt {
    private final int val;

    public UserId(int val) {
        this.val = val;
    }

    @Override
    public int getDbValue() {
        return val;
    }

    public static DbMapper<UserId> MAPPER = (r) -> new UserId(r.getInt(1));
}
