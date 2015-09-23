package com.github.mjdbc.test.asset.sql;


import com.github.mjdbc.Bind;
import com.github.mjdbc.Sql;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Sql interface to test Java String type.
 */

public interface JavaTypesStringSql {

    @Nullable
    @Sql("SELECT varchar_field FROM custom_types")
    String getNullableVarcharString();

    @Nullable
    @Sql("SELECT char_field FROM custom_types")
    String getNullableCharString();

    @NotNull
    @Sql("SELECT varchar_field_nn FROM custom_types")
    String getNotNullableVarcharString();

    @NotNull
    @Sql("SELECT char_field_nn FROM custom_types")
    String getNotNullableCharString();

    @Sql("UPDATE custom_types SET varchar_field = :value")
    void setNullableVarcharString(@Nullable @Bind("value") String v);

    @Sql("UPDATE custom_types SET char_field = :value")
    void setNullableCharString(@Nullable @Bind("value") String v);

    @Sql("UPDATE custom_types SET varchar_field_nn = :value")
    void setNotNullableVarcharString(@NotNull @Bind("value") String v);

    @Sql("UPDATE custom_types SET char_field_nn = :value")
    void setNotNullableCharString(@NotNull @Bind("value") String v);
}
