package com.github.mjdbc.test.asset.model;

import com.github.mjdbc.type.impl.AbstractDbInt;
import com.github.mjdbc.DbMapper;

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

    public static final DbMapper<UserId> MAPPER = (r) -> new UserId(r.getInt(1));
}
