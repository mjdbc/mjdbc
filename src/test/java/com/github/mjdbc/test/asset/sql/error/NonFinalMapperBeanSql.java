package com.github.mjdbc.test.asset.sql.error;

import com.github.mjdbc.Sql;
import com.github.mjdbc.test.asset.model.BeanWithStaticFieldMapper;
import com.github.mjdbc.test.asset.model.error.NonFinalMapperBean;

public interface NonFinalMapperBeanSql {

    @Sql("SELECT 1")
    NonFinalMapperBean select();

}
