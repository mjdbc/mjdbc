package com.github.mjdbc;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Set of all 'mJDBC' mappers.
 */
public final class Mappers {

    public static final DbMapper<BigDecimal> BigDecimalMapper = r -> r.getBigDecimal(1);

    public static final DbMapper<Boolean> BooleanMapper = r -> getNullable(r, r.getBoolean(1));

    public static final DbMapper<Byte> ByteMapper = r -> getNullable(r, r.getByte(1));

    public static final DbMapper<Character> CharacterMapper = r -> getNullable(r, (char) r.getInt(1));

    public static final DbMapper<Double> DoubleMapper = r -> getNullable(r, r.getDouble(1));

    public static final DbMapper<Float> FloatMapper = r -> getNullable(r, r.getFloat(1));

    public static final DbMapper<Integer> IntegerMapper = r -> getNullable(r, r.getInt(1));

    public static final DbMapper<Long> LongMapper = r -> getNullable(r, r.getLong(1));

    public static final DbMapper<Short> ShortMapper = r -> getNullable(r, r.getShort(1));

    public static final DbMapper<java.sql.Date> SqlDateMapper = r -> r.getDate(1);

    public static final DbMapper<java.sql.Time> SqlTimeMapper = r -> r.getTime(1);

    public static final DbMapper<String> StringMapper = r -> r.getString(1);

    public static final DbMapper<Timestamp> TimestampMapper = r -> r.getTimestamp(1);

    public static final DbMapper<java.util.Date> UtilDateMapper = r -> r.getDate(1);

    public static final DbMapper<Void> VoidMapper = r -> null;

    public static final Map<Class, DbMapper> BUILT_IN_MAPPERS = Collections.unmodifiableMap(new HashMap<Class, DbMapper>() {{

        // void methods.
        put(Void.TYPE, VoidMapper);
        put(Void.class, VoidMapper);

        // primitive types
        put(Boolean.TYPE, BooleanMapper);
        put(Boolean.class, BooleanMapper);
        put(Byte.TYPE, ByteMapper);
        put(Byte.class, ByteMapper);
        put(Character.TYPE, CharacterMapper);
        put(Character.class, CharacterMapper);
        put(Double.TYPE, DoubleMapper);
        put(Double.class, DoubleMapper);
        put(Short.TYPE, ShortMapper);
        put(Short.class, ShortMapper);
        put(Float.TYPE, FloatMapper);
        put(Float.class, FloatMapper);
        put(Integer.TYPE, IntegerMapper);
        put(Integer.class, IntegerMapper);
        put(Long.TYPE, LongMapper);
        put(Long.class, LongMapper);

        // jdbc related types
        put(BigDecimal.class, BigDecimalMapper);
        put(java.util.Date.class, UtilDateMapper);
        put(java.sql.Date.class, SqlDateMapper);
        put(java.sql.Time.class, SqlTimeMapper);
        put(Timestamp.class, TimestampMapper);

        // Strings
        put(String.class, StringMapper);

        // Collection wrappers are processed separately.
    }});

    public static <T> T getNullable(ResultSet r, T value) throws SQLException {
        return r.wasNull() ? null : value;
    }

}
