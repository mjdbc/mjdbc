package com.github.mjdbc.test.asset.sql;


import com.github.mjdbc.Batch;
import com.github.mjdbc.Bind;
import com.github.mjdbc.Sql;
import com.github.mjdbc.test.asset.model.User;

import java.util.Iterator;
import java.util.List;

/**
 * Sql interface to test batch methods .
 */
public interface BatchSql {

    @Sql("SELECT * FROM user")
    List<User> selectAllUsers();


    @Batch(10)
    @Sql("UPDATE user SET score = 100 WHERE id=:id")
    void batchUpdateWithCollection(@Bind("id") List<Integer> ids);

    @Batch(10)
    @Sql("UPDATE user SET score = 100 WHERE id=:id")
    void batchUpdateWithIterator(@Bind("id") Iterator<Integer> ids);

    @Batch(10)
    @Sql("UPDATE user SET score = 100 WHERE id=:id")
    void batchUpdateWithPrimitiveArray(@Bind("id") int[] ids);

    @Batch(10)
    @Sql("UPDATE user SET score = 100 WHERE id=:id")
    void batchUpdateWithObjectArray(@Bind("id") Integer[] ids);

}
