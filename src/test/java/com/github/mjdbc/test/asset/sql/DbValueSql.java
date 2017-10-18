package com.github.mjdbc.test.asset.sql;


import com.github.mjdbc.Bind;
import com.github.mjdbc.Sql;
import com.github.mjdbc.type.DbInt;
import com.github.mjdbc.type.DbLong;
import com.github.mjdbc.type.DbString;
import com.github.mjdbc.type.DbTimestamp;
import java.sql.Timestamp;
import org.jetbrains.annotations.Nullable;

/**
 * Sql interface to test various Java types.
 */
public interface DbValueSql {

    @Sql("SELECT integer_field FROM custom_types")
    Integer getNullableInt();

    @Sql("UPDATE custom_types SET integer_field = :value")
    void setNullableDbInt(@Bind("value") @Nullable DbInt value);


    @Sql("SELECT long_field FROM custom_types")
    Long getNullableLong();

    @Sql("UPDATE custom_types SET long_field = :value")
    void setNullableDbLong(@Bind("value") @Nullable DbLong value);


    @Sql("SELECT varchar_field FROM custom_types")
    String getNullableString();

    @Sql("UPDATE custom_types SET varchar_field = :value")
    void setNullableDbString(@Bind("value") @Nullable DbString value);


    @Sql("SELECT timestamp_field FROM custom_types")
    Timestamp getNullableTimestamp();

    @Sql("UPDATE custom_types SET timestamp_field = :value")
    void setNullableDbTimestamp(@Bind("value") @Nullable DbTimestamp value);
}
