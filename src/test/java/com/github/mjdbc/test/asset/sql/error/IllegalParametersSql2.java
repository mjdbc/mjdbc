package com.github.mjdbc.test.asset.sql.error;


import com.github.mjdbc.Bind;
import com.github.mjdbc.Sql;
import com.github.mjdbc.test.asset.model.UserId;

/**
 * Broken Sql interface used for testing.
 */
public interface IllegalParametersSql2 {

    @Sql("SELECT login FROM users WHERE id = : val")
    void update(@Bind(" val") UserId id);

}
