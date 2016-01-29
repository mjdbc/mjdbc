package com.github.mjdbc.test.asset.sql.error;


import com.github.mjdbc.Bind;
import com.github.mjdbc.Sql;

import java.util.List;

/**
 * Sql interface to test batch methods error handling
 */
public interface BatchSqlErr4 {

    @Sql("UPDATE users SET score = :score WHERE id=:id")
    void batchUpdateWithCollection(@Bind("id") List<Class> ids, @Bind("score") int scores);
}
