package com.github.mjdbc.util;

import com.github.mjdbc.DbBinder;
import com.github.mjdbc.DbMapper;
import com.github.mjdbc.binder.JavaTypeBinder;
import com.github.mjdbc.mapper.JavaTypeMapper;
import org.jetbrains.annotations.NotNull;

/**
 * List of Java types supported by 'mjdbc' binders and mappers.
 */
public enum JavaType {
    BigDecimal,
    Boolean,
    Byte,
    Character,
    Date,
    Double,
    Float,
    Integer,
    Long,
    Short,
    SqlDate,
    SqlTime,
    String,
    Timestamp,
    Void;

    @NotNull
    public final DbBinder binder;

    @NotNull
    public final DbMapper mapper;

    JavaType() {
        binder = new JavaTypeBinder(this);
        mapper = new JavaTypeMapper(this);
    }
}
