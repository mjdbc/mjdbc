package com.github.mjdbc.test.asset.sql.error;

import com.github.mjdbc.Bind;
import com.github.mjdbc.Sql;
import com.github.mjdbc.test.asset.model.error.AmbiguousTypeValue;

public interface AmbiguousTypeSql {
    @Sql("SELECT count(*) FROM users WHERE id = :id")
    int call(@Bind("id") AmbiguousTypeValue value);
}
