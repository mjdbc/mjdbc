package mini.jdbc;

import mini.jdbc.binder.DbValueBinder;
import mini.jdbc.binder.JavaTypeBinder;
import mini.jdbc.binder.JavaType;
import mini.jdbc.type.DbInt;
import mini.jdbc.type.DbLong;
import mini.jdbc.type.DbString;

import java.math.BigDecimal;
import java.security.Timestamp;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class Binders {

    public static final Map<Class, DbParameterBinder> BUILT_IN_BINDERS = Collections.unmodifiableMap(new HashMap<Class, DbParameterBinder>() {{

        //primitive types
        put(Boolean.class, new JavaTypeBinder(JavaType.Boolean));
        put(Byte.class, new JavaTypeBinder(JavaType.Byte));
        put(Character.class, new JavaTypeBinder(JavaType.Character));
        put(Double.class, new JavaTypeBinder(JavaType.Double));
        put(Short.class, new JavaTypeBinder(JavaType.Short));
        put(Float.class, new JavaTypeBinder(JavaType.Float));
        put(Integer.class, new JavaTypeBinder(JavaType.Integer));
        put(Long.class, new JavaTypeBinder(JavaType.Long));

        // Sql relate types
        put(BigDecimal.class, new JavaTypeBinder(JavaType.BigDecimal));
        put(java.util.Date.class, new JavaTypeBinder(JavaType.Date));
        put(java.sql.Date.class, new JavaTypeBinder(JavaType.SqlDate));
        put(Timestamp.class, new JavaTypeBinder(JavaType.Timestamp));

        // String classes
        put(String.class, new JavaTypeBinder(JavaType.String));
        put(StringBuilder.class, new JavaTypeBinder(JavaType.String));
        put(StringBuffer.class, new JavaTypeBinder(JavaType.String));
        put(CharSequence.class, new JavaTypeBinder(JavaType.String));

        // Custom types
        put(DbInt.class, new DbValueBinder(JavaType.Integer));
        put(DbLong.class, new DbValueBinder(JavaType.Long));
        put(DbString.class, new DbValueBinder(JavaType.String));
    }});
}
