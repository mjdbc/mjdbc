package com.github.mjdbc.test.asset.sql.error;


import com.github.mjdbc.Bind;
import com.github.mjdbc.Sql;

import java.util.List;

/**
 * Sql interface to test batch methods error handling
 */
public interface BatchSqlErr2 {

    @Sql("UPDATE users SET score = :score WHERE id=:id")
    void batchUpdateWithCollection(@Bind("id") List<Integer> ids, @Bind("score") List<Integer> scores);
}
