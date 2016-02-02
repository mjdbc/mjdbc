package com.github.mjdbc.test.asset.sql.error;


import com.github.mjdbc.Bind;
import com.github.mjdbc.Sql;

import java.util.List;

public interface NonVoidBatchMethodSql {

    @Sql("UPDATE users SET score = :score WHERE id = :id")
    int batchUpdateWithCollection(@Bind("id") List<Integer> ids, @Bind("score") int score);
}
