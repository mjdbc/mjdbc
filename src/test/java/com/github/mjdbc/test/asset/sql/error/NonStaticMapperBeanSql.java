package com.github.mjdbc.test.asset.sql.error;

import com.github.mjdbc.Sql;
import com.github.mjdbc.test.asset.model.error.NonFinalMapperBean;
import com.github.mjdbc.test.asset.model.error.NonStaticMapperBean;

public interface NonStaticMapperBeanSql {

    @Sql("SELECT 1")
    NonStaticMapperBean select();

}
