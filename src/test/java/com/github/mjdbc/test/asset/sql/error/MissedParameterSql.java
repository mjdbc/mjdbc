package com.github.mjdbc.test.asset.sql.error;


import com.github.mjdbc.Sql;

/**
 * Sql interface used to test handling of missed parameters.
 */
public interface MissedParameterSql {

    @Sql("SELECT * FROM users WHERE login = :y")
    void updateFirstNameWithReader();
}
