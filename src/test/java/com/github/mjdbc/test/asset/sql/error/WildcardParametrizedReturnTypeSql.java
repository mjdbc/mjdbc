package com.github.mjdbc.test.asset.sql.error;

import com.github.mjdbc.Sql;
import com.github.mjdbc.test.asset.model.User;

import java.util.List;

public interface WildcardParametrizedReturnTypeSql {
    @Sql("SELECT * FROM users")
    List<? extends User> selectAll();
}
