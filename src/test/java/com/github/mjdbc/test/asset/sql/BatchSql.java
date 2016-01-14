package com.github.mjdbc.test.asset.sql;


import com.github.mjdbc.Bind;
import com.github.mjdbc.BindBean;
import com.github.mjdbc.Sql;
import com.github.mjdbc.test.asset.model.User;

import java.util.Iterator;
import java.util.List;

/**
 * Sql interface to test batch methods .
 */
public interface BatchSql {

    @Sql("SELECT * FROM users")
    List<User> selectAllUsers();


    @Sql("UPDATE users SET score = :score WHERE id=:id")
    void batchUpdateWithCollection(@Bind("id") List<Integer> ids, @Bind("score") int score);

    @Sql("UPDATE users SET score = :score WHERE id=:id")
    void batchUpdateWithIterator(@Bind("id") Iterator<Integer> ids, @Bind("score") int score);

    @Sql("UPDATE users SET score = :score WHERE id=:id")
    void batchUpdateWithPrimitiveArray(@Bind("id") int[] ids, @Bind("score") int score);

    @Sql("UPDATE users SET score = :score WHERE id=:id")
    void batchUpdateWithObjectArray(@Bind("id") Integer[] ids, @Bind("score") int score);

    @Sql("UPDATE users SET score = :score WHERE id=:id")
    void batchUpdateBeanWithIterable(@BindBean Iterable<User> users);

    @Sql("UPDATE users SET score = :score WHERE id=:id")
    void batchUpdateBeanWithIterator(@BindBean Iterator<User> users);

    @Sql("UPDATE users SET score = :score WHERE id=:id")
    void batchUpdateBeanWithArray(@BindBean User[] users);
}
