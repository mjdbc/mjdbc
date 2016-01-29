package com.github.mjdbc.test.asset.sql.error;


import com.github.mjdbc.Bind;
import com.github.mjdbc.Sql;

/**
 * Broken Sql interface used for testing.
 */
public interface UnboundParameterSql {

    @Sql("SELECT 1")
    void update(@Bind("p") Class c);
}
