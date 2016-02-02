package com.github.mjdbc.test.asset.model;

import com.github.mjdbc.DbMapper;
import com.github.mjdbc.Mapper;
import com.github.mjdbc.type.impl.DbIntValue;

/**
 * Type safe id for User objects.
 */
public final class UserId extends DbIntValue {
    public UserId(int val) {
        super(val);
    }

    @Mapper
    public static final DbMapper<UserId> MAPPER = (r) -> new UserId(r.getInt(1));
}
