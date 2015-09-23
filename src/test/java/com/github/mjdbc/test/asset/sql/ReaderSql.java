package com.github.mjdbc.test.asset.sql;


import com.github.mjdbc.Bind;
import com.github.mjdbc.Sql;

import java.io.Reader;

/**
 * Sql interface used to test custom binders.
 */
public interface ReaderSql {

    @Sql("UPDATE users SET first_name = :firstName  WHERE login = :login")
    void updateFirstNameWithReader(@Bind("login") String login, @Bind("firstName") Reader r);
}
