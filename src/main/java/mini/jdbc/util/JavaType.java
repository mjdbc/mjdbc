package mini.jdbc.util;

import mini.jdbc.DbBinder;
import mini.jdbc.DbMapper;
import mini.jdbc.binder.JavaTypeBinder;
import mini.jdbc.mapper.JavaTypeMapper;
import org.jetbrains.annotations.NotNull;

/**
 * List of Java types supported by 'mini-jdbc' binders and mappers.
 */
public enum JavaType {
    BigDecimal,
    Boolean,
    Byte,
    Character,
    Date,
    Double,
    Short,
    Float,
    Integer,
    Long,
    SqlDate,
    String,
    Timestamp;

    @NotNull
    public final DbBinder binder;

    @NotNull
    public final DbMapper mapper;

    JavaType() {
        binder = new JavaTypeBinder(this);
        mapper = new JavaTypeMapper(this);
    }
}
