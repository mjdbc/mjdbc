package com.github.mjdbc.test.asset;


import com.github.mjdbc.Sql;

/**
 * Set of raw SQL method to access to 'user' table data.
 */
public interface EmptyQuerySql {

    @Sql("")
    void updateFirstNameWithReader();
}
