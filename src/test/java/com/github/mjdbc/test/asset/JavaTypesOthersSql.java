package com.github.mjdbc.test.asset;


import com.github.mjdbc.Bind;
import com.github.mjdbc.Sql;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;

public interface JavaTypesOthersSql {

    /* BigDecimal */
    @Sql("SELECT big_decimal_field FROM custom_types")
    BigDecimal getNullableBigDecimal();

    @Sql("UPDATE custom_types SET big_decimal_field = :value")
    void setNullableBigDecimal(@Bind("value") @Nullable BigDecimal value);


    /* Boolean */
    @Sql("SELECT boolean_field FROM custom_types")
    boolean getNullableBooleanAsBool();

    @Sql("SELECT boolean_field FROM custom_types")
    Boolean getNullableBoolean();

    @Sql("UPDATE custom_types SET boolean_field = :value")
    void setNullableBoolean(@Bind("value") @Nullable Byte value);

    @Sql("UPDATE custom_types SET boolean_field = :value")
    void setNullableBooleanAsBool(@Bind("value") boolean value);


    /* Byte */
    @Sql("SELECT byte_field FROM custom_types")
    byte getNullableByteAsByte();

    @Sql("SELECT byte_field FROM custom_types")
    Byte getNullableByte();

    @Sql("UPDATE custom_types SET byte_field = :value")
    void setNullableByte(@Bind("value") @Nullable Byte value);

    @Sql("UPDATE custom_types SET byte_field = :value")
    void setNullableByteAsByte(@Bind("value") byte value);


    /* Character */
    @Sql("SELECT character_field FROM custom_types")
    char getNullableCharacterAsChar();

    @Sql("SELECT character_field FROM custom_types")
    Character getNullableCharacter();

    @Sql("UPDATE custom_types SET character_field = :value")
    void setNullableCharacter(@Bind("value") @Nullable Character value);

    @Sql("UPDATE custom_types SET character_field = :value")
    void setNullableCharacterAsChar(@Bind("value") char value);


    /* java.util.Date and java.sql.Date */
    @Sql("SELECT date_field FROM custom_types")
    java.util.Date getNullableDate();

    @Sql("UPDATE custom_types SET date_field = :value")
    void setNullableDate(@Bind("value") @Nullable java.util.Date value);

    @Sql("SELECT date_field FROM custom_types")
    java.sql.Date getNullableSqlDate();

    @Sql("UPDATE custom_types SET date_field = :value")
    void setNullableSqlDate(@Bind("value") @Nullable java.sql.Date value);


    /* Double */
    @Sql("SELECT double_field FROM custom_types")
    double getNullableDoubleAsDouble();

    @Sql("SELECT double_field FROM custom_types")
    Double getNullableDouble();

    @Sql("UPDATE custom_types SET double_field = :value")
    void setNullableDouble(@Bind("value") @Nullable Double value);

    @Sql("UPDATE custom_types SET double_field = :value")
    void setNullableDoubleAsDouble(@Bind("value") double value);


    /* Float */
    @Sql("SELECT float_field FROM custom_types")
    float getNullableFloatAsFloat();

    @Sql("SELECT float_field FROM custom_types")
    Float getNullableFloat();

    @Sql("UPDATE custom_types SET float_field = :value")
    void setNullableFloat(@Bind("value") @Nullable Float value);

    @Sql("UPDATE custom_types SET float_field = :value")
    void setNullableFloatAsFloat(@Bind("value") float value);


    /* Long */
    @Sql("SELECT long_field FROM custom_types")
    long getNullableLongAsLong();

    @Sql("SELECT long_field FROM custom_types")
    Long getNullableLong();

    @Sql("UPDATE custom_types SET long_field = :value")
    void setNullableLong(@Bind("value") @Nullable Long value);

    @Sql("UPDATE custom_types SET long_field = :value")
    void setNullableLongAsLong(@Bind("value") long value);


    /* Short */
    @Sql("SELECT short_field FROM custom_types")
    short getNullableShortAsShort();

    @Sql("SELECT short_field FROM custom_types")
    Short getNullableShort();

    @Sql("UPDATE custom_types SET short_field = :value")
    void setNullableShort(@Bind("value") @Nullable Short value);

    @Sql("UPDATE custom_types SET short_field = :value")
    void setNullableShortAsShort(@Bind("value") short value);


    /* Time */
    @Sql("SELECT time_field FROM custom_types")
    Time getNullableTime();

    @Sql("UPDATE custom_types SET time_field = :value")
    void setNullableTime(@Bind("value") @Nullable Time value);


    /* Timestamp */
    @Sql("SELECT timestamp_field FROM custom_types")
    Timestamp getNullableTimestamp();

    @Sql("UPDATE custom_types SET timestamp_field = :value")
    void setNullableTimestamp(@Bind("value") @Nullable Timestamp value);
}
