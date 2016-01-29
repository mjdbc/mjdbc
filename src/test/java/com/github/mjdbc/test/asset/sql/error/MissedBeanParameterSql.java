package com.github.mjdbc.test.asset.sql.error;


import com.github.mjdbc.BindBean;
import com.github.mjdbc.Sql;
import com.github.mjdbc.test.asset.model.User;

/**
 * Sql interface used to test missed bean-level parameters.
 */
public interface MissedBeanParameterSql {

    @Sql("SELECT * FROM users WHERE login = :loogin")
    void updateFirstNameWithReader(@BindBean User user);
}
