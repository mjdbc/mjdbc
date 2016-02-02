package com.github.mjdbc.test.asset.sql.error;


import com.github.mjdbc.Bind;
import com.github.mjdbc.Sql;

public interface MultipleBatchParams2Sql {

    @Sql("UPDATE users SET score = :score WHERE id=:id")
    void batchUpdateWithCollection(@Bind("id") Integer[] ids, @Bind("score") int[] scores);
}
