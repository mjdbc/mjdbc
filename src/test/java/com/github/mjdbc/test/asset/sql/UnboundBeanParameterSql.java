package com.github.mjdbc.test.asset.sql;


import com.github.mjdbc.BindBean;
import com.github.mjdbc.Sql;
import com.github.mjdbc.test.asset.model.Bean1;

/**
 * Broken Sql interface used for testing.
 */
public interface UnboundBeanParameterSql {

    @Sql("SELECT login FROM users WHERE id = :unmappedValue")
    void update(@BindBean Bean1 b);
}
