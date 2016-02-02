package com.github.mjdbc.test.asset.sql.error;

import com.github.mjdbc.Sql;
import com.github.mjdbc.test.asset.model.error.NonFinalMapperBean;
import com.github.mjdbc.test.asset.model.error.NonPublicMapperBean;

public interface NonPublicMapperBeanSql {

    @Sql("SELECT 1")
    NonPublicMapperBean select();

}
