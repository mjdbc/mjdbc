package com.github.mjdbc.test.asset.sql;


import com.github.mjdbc.Bind;
import com.github.mjdbc.Sql;

/**
 * Sql interface to test batch methods error handling
 */
public interface BatchSqlErr3 {

    @Sql("UPDATE users SET score = :score WHERE id=:id")
    void batchUpdateWithCollection(@Bind("id") Integer[] ids, @Bind("score") int[] scores);
}
