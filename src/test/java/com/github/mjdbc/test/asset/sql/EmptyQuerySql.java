package com.github.mjdbc.test.asset.sql;


import com.github.mjdbc.Sql;

/**
 * Sql interface to check empty query .
 */
public interface EmptyQuerySql {

    @Sql
    void updateFirstNameWithReader();
}
