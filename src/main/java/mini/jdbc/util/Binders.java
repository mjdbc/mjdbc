package mini.jdbc.util;

import mini.jdbc.DbBinder;
import mini.jdbc.binder.DbValueBinder;
import mini.jdbc.type.DbInt;
import mini.jdbc.type.DbLong;
import mini.jdbc.type.DbString;
import mini.jdbc.util.JavaType;

import java.math.BigDecimal;
import java.security.Timestamp;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Map of all 'mini-jdbc' binders in a single place.
 */
public final class Binders {

    public static final Map<Class, DbBinder> BUILT_IN_BINDERS = Collections.unmodifiableMap(new HashMap<Class, DbBinder>() {{

        // primitive types
        put(Boolean.TYPE, JavaType.Boolean.binder);
        put(Boolean.class, JavaType.Boolean.binder);
        put(Byte.TYPE, JavaType.Byte.binder);
        put(Byte.class, JavaType.Byte.binder);
        put(Character.TYPE, JavaType.Character.binder);
        put(Character.class, JavaType.Character.binder);
        put(Double.TYPE, JavaType.Double.binder);
        put(Double.class, JavaType.Double.binder);
        put(Short.TYPE, JavaType.Short.binder);
        put(Short.class, JavaType.Short.binder);
        put(Float.TYPE, JavaType.Float.binder);
        put(Float.class, JavaType.Float.binder);
        put(Integer.TYPE, JavaType.Integer.binder);
        put(Integer.class, JavaType.Integer.binder);
        put(Long.TYPE, JavaType.Long.binder);
        put(Long.class, JavaType.Long.binder);

        // jdbc related types
        put(BigDecimal.class, JavaType.BigDecimal.binder);
        put(java.util.Date.class, JavaType.Date.binder);
        put(java.sql.Date.class, JavaType.SqlDate.binder);
        put(Timestamp.class, JavaType.Timestamp.binder);

        // Strings
        put(String.class, JavaType.String.binder);
        put(StringBuilder.class, JavaType.String.binder);
        put(StringBuffer.class, JavaType.String.binder);
        put(CharSequence.class, JavaType.String.binder);

        // Helper types
        put(DbInt.class, new DbValueBinder(JavaType.Integer));
        put(DbLong.class, new DbValueBinder(JavaType.Long));
        put(DbString.class, new DbValueBinder(JavaType.String));
    }});
}
