package com.github.mjdbc.test.asset.sql;


import com.github.mjdbc.Bind;
import com.github.mjdbc.Sql;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Sql interface to test Integer type.
 */
public interface JavaTypesIntegerSql {

    @Sql("SELECT integer_field FROM custom_types")
    int getNullableIntegerAsInt();

    @Sql("SELECT integer_field FROM custom_types")
    Integer getNullableInteger();

    @Sql("UPDATE custom_types SET integer_field = :value")
    void setNullableInteger(@Bind("value") @Nullable Integer value);

    @Sql("SELECT integer_field_nn FROM custom_types")
    Integer getNotNullableInteger();

    @Sql("SELECT integer_field_nn FROM custom_types")
    int getNotNullableIntegerAsInt();

    @Sql("UPDATE custom_types SET integer_field_nn = :value")
    void setNotNullableInteger(@Bind("value") @NotNull Integer value);

}
