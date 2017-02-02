package com.github.mjdbc.test.asset.sql;

import com.github.mjdbc.BindBean;
import com.github.mjdbc.GetGeneratedKeys;
import com.github.mjdbc.Sql;
import com.github.mjdbc.test.asset.model.FinalBean;

public interface FinalBeanSql {

    @GetGeneratedKeys
    @Sql("INSERT INTO bean(boolean_field, int_field, string_field, int_value_field) VALUES (:booleanField, :intField, :stringField, :intValueField)")
    long insert(@BindBean FinalBean bean);

    @Sql("UPDATE bean SET boolean_field = :booleanField, int_field = :intField, string_field = :stringField, int_value_field = :intValueField WHERE id = :id")
    int update(@BindBean FinalBean bean);

}
