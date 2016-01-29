package com.github.mjdbc.test.asset.sql.error;


import com.github.mjdbc.Bind;
import com.github.mjdbc.Sql;
import com.github.mjdbc.test.asset.model.UserId;

/**
 * Broken Sql interface used for testing.
 */
public interface DuplicateParametersSql {

    @Sql("SELECT login FROM users WHERE id = :val AND login = :val")
    void update(@Bind("val") UserId id, @Bind("val") String login);
}
