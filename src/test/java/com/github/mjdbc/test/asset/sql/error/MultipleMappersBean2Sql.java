package com.github.mjdbc.test.asset.sql.error;


import com.github.mjdbc.Sql;
import com.github.mjdbc.test.asset.model.error.MultipleMappersBean2;

/**
 * Broken Sql interface used for testing.
 */
public interface MultipleMappersBean2Sql {

    @Sql("SELECT id FROM users WHERE id = 1")
    MultipleMappersBean2 selectABean();
}
