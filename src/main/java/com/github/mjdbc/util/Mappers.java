package com.github.mjdbc.util;

import com.github.mjdbc.DbMapper;
import com.github.mjdbc.mapper.VoidMapper;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Map of all 'mjdbc' mappers in single place.
 */
public final class Mappers {

    public static final Map<Class, DbMapper> BUILT_IN_MAPPERS = Collections.unmodifiableMap(new HashMap<Class, DbMapper>() {{

        // void methods.
        put(Void.TYPE, VoidMapper.INSTANCE);
        put(Void.class, VoidMapper.INSTANCE);

        // primitive types
        put(Boolean.TYPE, JavaType.Boolean.mapper);
        put(Boolean.class, JavaType.Boolean.mapper);
        put(Byte.TYPE, JavaType.Byte.mapper);
        put(Byte.class, JavaType.Byte.mapper);
        put(Character.TYPE, JavaType.Character.mapper);
        put(Character.class, JavaType.Character.mapper);
        put(Double.TYPE, JavaType.Double.mapper);
        put(Double.class, JavaType.Double.mapper);
        put(Short.TYPE, JavaType.Short.mapper);
        put(Short.class, JavaType.Short.mapper);
        put(Float.TYPE, JavaType.Float.mapper);
        put(Float.class, JavaType.Float.mapper);
        put(Integer.TYPE, JavaType.Integer.mapper);
        put(Integer.class, JavaType.Integer.mapper);
        put(Long.TYPE, JavaType.Long.mapper);
        put(Long.class, JavaType.Long.mapper);

        // jdbc related types
        put(BigDecimal.class, JavaType.BigDecimal.mapper);
        put(java.util.Date.class, JavaType.Date.mapper);
        put(java.sql.Date.class, JavaType.SqlDate.mapper);
        put(java.sql.Time.class, JavaType.SqlTime.mapper);
        put(Timestamp.class, JavaType.Timestamp.mapper);

        // Strings
        put(String.class, JavaType.String.mapper);

        // Collection wrappers are processed separately.
    }});
}
