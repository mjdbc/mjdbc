package com.github.mjdbc.test.asset.sql.error;

import com.github.mjdbc.Sql;
import com.github.mjdbc.test.asset.model.error.NonStaticMapperBean;

public interface BeanWithNullMapperSql {

    @Sql("SELECT 1")
    NonStaticMapperBean select();

}
