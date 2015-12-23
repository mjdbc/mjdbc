package com.github.mjdbc;

import com.github.mjdbc.type.DbInt;
import com.github.mjdbc.type.DbLong;
import com.github.mjdbc.type.DbString;
import com.github.mjdbc.type.DbTimestamp;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Map of all 'mjdbc' binders in a single place.
 */
@SuppressWarnings("AccessCanBeTightened")
public final class Binders {

    public static final DbBinder<BigDecimal> BigDecimalBinder = PreparedStatement::setBigDecimal;

    public static final DbBinder<Boolean> BooleanBinder = (statement, idx, value) -> {
        if (value == null) {
            statement.setNull(idx, Types.BOOLEAN);
        } else {
            statement.setBoolean(idx, value);
        }
    };
    public static final DbBinder<Byte> ByteBinder = (statement, idx, value) -> {
        if (value == null) {
            statement.setNull(idx, Types.INTEGER);
        } else {
            statement.setByte(idx, value);
        }
    };
    public static final DbBinder<Character> CharacterBinder = (statement, idx, value) -> {
        if (value == null) {
            statement.setNull(idx, Types.INTEGER);
        } else {
            statement.setInt(idx, value);
        }
    };
    public static final DbBinder<Double> DoubleBinder = (statement, idx, value) -> {
        if (value == null) {
            statement.setNull(idx, Types.DOUBLE);
        } else {
            statement.setDouble(idx, value);
        }
    };

    public static final DbBinder<Float> FloatBinder = (statement, idx, value) -> {
        if (value == null) {
            statement.setNull(idx, Types.FLOAT);
        } else {
            statement.setFloat(idx, value);
        }
    };
    public static final DbBinder<Integer> IntegerBinder = (statement, idx, value) -> {
        if (value == null) {
            statement.setNull(idx, Types.INTEGER);
        } else {
            statement.setInt(idx, value);
        }
    };
    public static final DbBinder<Long> LongBinder = (statement, idx, value) -> {
        if (value == null) {
            statement.setNull(idx, Types.BIGINT);
        } else {
            statement.setLong(idx, value);
        }
    };
    public static final DbBinder<Short> ShortBinder = (statement, idx, value) -> {
        if (value == null) {
            statement.setNull(idx, Types.INTEGER);
        } else {
            statement.setShort(idx, value);
        }
    };
    public static final DbBinder<java.sql.Date> SqlDateBinder = PreparedStatement::setDate;
    public static final DbBinder<java.sql.Time> SqlTimeBinder = PreparedStatement::setTime;
    public static final DbBinder<String> StringBinder = PreparedStatement::setString;
    public static final DbBinder<Timestamp> TimestampBinder = PreparedStatement::setTimestamp;
    public static final DbBinder<java.util.Date> UtilDateBinder = (statement, idx, value) -> statement.setDate(idx, value == null ? null : new java.sql.Date(value.getTime()));

    public static final DbBinder<DbInt> DbIntBinder = (statement, idx, value) -> IntegerBinder.bind(statement, idx, value == null ? null : value.getDbValue());
    public static final DbBinder<DbLong> DbLongBinder = (statement, idx, value) -> LongBinder.bind(statement, idx, value == null ? null : value.getDbValue());
    public static final DbBinder<DbString> DbStringBinder = (statement, idx, value) -> StringBinder.bind(statement, idx, value == null ? null : value.getDbValue());
    public static final DbBinder<DbTimestamp> DbTimestampBinder = (statement, idx, value) -> TimestampBinder.bind(statement, idx, value == null ? null : value.getDbValue());

    public static final Map<Class, DbBinder> BUILT_IN_BINDERS = Collections.unmodifiableMap(new HashMap<Class, DbBinder>() {{

        // primitive types
        put(Boolean.TYPE, BooleanBinder);
        put(Boolean.class, BooleanBinder);
        put(Byte.TYPE, ByteBinder);
        put(Byte.class, ByteBinder);
        put(Character.TYPE, CharacterBinder);
        put(Character.class, CharacterBinder);
        put(Double.TYPE, DoubleBinder);
        put(Double.class, DoubleBinder);
        put(Short.TYPE, ShortBinder);
        put(Short.class, ShortBinder);
        put(Float.TYPE, FloatBinder);
        put(Float.class, FloatBinder);
        put(Integer.TYPE, IntegerBinder);
        put(Integer.class, IntegerBinder);
        put(Long.TYPE, LongBinder);
        put(Long.class, LongBinder);

        // jdbc related types
        put(BigDecimal.class, BigDecimalBinder);
        put(java.sql.Date.class, SqlDateBinder);
        put(java.sql.Time.class, SqlTimeBinder);
        put(Timestamp.class, TimestampBinder);
        put(java.util.Date.class, UtilDateBinder);

        // Strings
        put(String.class, StringBinder);
        put(StringBuilder.class, StringBinder);
        put(StringBuffer.class, StringBinder);
        put(CharSequence.class, StringBinder);

        // Helper types
        put(DbInt.class, DbIntBinder);
        put(DbLong.class, DbLongBinder);
        put(DbString.class, DbStringBinder);
        put(DbTimestamp.class, DbTimestampBinder);
    }});
}
