package com.github.mjdbc.test.asset.sql;

import com.github.mjdbc.Sql;
import com.github.mjdbc.test.asset.model.BeanWithStaticFieldMapper;

public interface BeanWithStaticFieldMapperSql {

    @Sql("SELECT 1")
    BeanWithStaticFieldMapper select();

}
