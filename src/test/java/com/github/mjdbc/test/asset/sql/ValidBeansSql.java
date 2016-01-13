package com.github.mjdbc.test.asset.sql;


import com.github.mjdbc.Sql;
import com.github.mjdbc.test.asset.model.ValidBean;

/**
 * Valid Sql interface used for testing.
 */
public interface ValidBeansSql {

    @Sql("SELECT id FROM users WHERE id = 1")
    ValidBean selectABean();
}
