package com.github.mjdbc.test.asset;


import com.github.mjdbc.Bind;
import com.github.mjdbc.Sql;

import java.io.Reader;

/**
 * Set of raw SQL method to access to 'user' table data.
 */
public interface ReaderSql {

    @Sql("UPDATE users SET first_name = :firstName  WHERE login = :login")
    void updateFirstNameWithReader(@Bind("login") String login, @Bind("firstName") Reader r);
}
