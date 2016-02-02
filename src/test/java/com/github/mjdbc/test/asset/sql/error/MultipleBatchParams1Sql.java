package com.github.mjdbc.test.asset.sql.error;


import com.github.mjdbc.Bind;
import com.github.mjdbc.Sql;

import java.util.List;

public interface MultipleBatchParams1Sql {

    @Sql("UPDATE users SET score = :score WHERE id=:id")
    void batchUpdateWithCollection(@Bind("id") List<Integer> ids, @Bind("score") List<Integer> scores);
}
