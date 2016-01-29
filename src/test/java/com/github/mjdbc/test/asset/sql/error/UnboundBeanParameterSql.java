package com.github.mjdbc.test.asset.sql.error;


import com.github.mjdbc.BindBean;
import com.github.mjdbc.Sql;
import com.github.mjdbc.test.asset.model.error.InvalidBean;

/**
 * Broken Sql interface used for testing.
 */
public interface UnboundBeanParameterSql {

    @Sql("SELECT login FROM users WHERE id = :unmappedValue")
    void update(@BindBean InvalidBean b);
}
