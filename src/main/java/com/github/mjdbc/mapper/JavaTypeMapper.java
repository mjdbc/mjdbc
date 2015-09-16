package com.github.mjdbc.mapper;

import com.github.mjdbc.DbMapper;
import com.github.mjdbc.util.JavaType;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 */
public final class JavaTypeMapper implements DbMapper {
    @NotNull
    private final JavaType type;

    public JavaTypeMapper(@NotNull JavaType type) {
        this.type = type;
    }

    @NotNull
    @Override
    public Object map(ResultSet r) throws SQLException {
        switch (type) {
            case BigDecimal:
                return r.getBigDecimal(1);
            case Boolean:
                return r.getBoolean(1);
            case Byte:
                return r.getByte(1);
            case Character:
                return (char) r.getInt(1);
            case Date:
                return r.getDate(1);
            case Double:
                return r.getDouble(1);
            case Short:
                return r.getShort(1);
            case Float:
                return r.getFloat(1);
            case Integer:
                return r.getInt(1);
            case Long:
                return r.getLong(1);
            case SqlDate:
                return r.getDate(1);
            case String:
                return r.getString(1);
            case Timestamp:
                return r.getTimestamp(1);
        }
        throw new IllegalArgumentException("Unsupported type: " + type);
    }

    @Override
    public String toString() {
        return "JavaTypeMapper[" + type + "]";
    }
}
