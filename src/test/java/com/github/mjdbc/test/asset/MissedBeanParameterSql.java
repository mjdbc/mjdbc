package com.github.mjdbc.test.asset;


import com.github.mjdbc.BindBean;
import com.github.mjdbc.Sql;
import com.github.mjdbc.test.asset.model.User;

/**
 * Set of raw SQL method to access to 'user' table data.
 */
public interface MissedBeanParameterSql {

    @Sql("SELECT * FROM users WHERE login = :loogin")
    void updateFirstNameWithReader(@BindBean User user);
}
