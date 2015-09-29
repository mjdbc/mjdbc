package com.github.mjdbc.test.asset.sql;


import com.github.mjdbc.Bind;
import com.github.mjdbc.Sql;
import com.github.mjdbc.test.asset.model.UserId;
import org.jetbrains.annotations.Nullable;

import java.io.Reader;

/**
 * Set of raw SQL method to access to 'user' table data.
 */
public interface InvalidMapperSql {

    @Nullable
    @Sql("SELECT * FROM users WHERE id = :id")
    Reader getUserById(@Bind("id") UserId id);
}
