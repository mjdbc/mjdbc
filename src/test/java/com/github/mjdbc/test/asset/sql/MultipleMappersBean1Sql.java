package com.github.mjdbc.test.asset.sql;


import com.github.mjdbc.Sql;
import com.github.mjdbc.test.asset.model.MultipleMappersBean1;

/**
 * Broken Sql interface used for testing.
 */
public interface MultipleMappersBean1Sql {

    @Sql("SELECT id FROM users WHERE id = 1")
    MultipleMappersBean1 selectABean();
}
