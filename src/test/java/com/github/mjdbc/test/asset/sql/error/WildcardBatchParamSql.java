package com.github.mjdbc.test.asset.sql.error;


import com.github.mjdbc.BindBean;
import com.github.mjdbc.Sql;
import com.github.mjdbc.test.asset.model.User;

import java.util.List;

public interface WildcardBatchParamSql {

    @Sql("UPDATE users SET score = :score WHERE id = :id")
    void batchUpdateWithCollection(@BindBean List<? extends User> users);
}
