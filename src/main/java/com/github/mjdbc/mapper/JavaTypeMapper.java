package com.github.mjdbc.mapper;

import com.github.mjdbc.DbMapper;
import com.github.mjdbc.util.JavaType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

    @Nullable
    @Override
    public Object map(ResultSet r) throws SQLException {
        Object result;
        switch (type) {
            case BigDecimal:
                result = r.getBigDecimal(1);
                break;
            case Boolean:
                result = r.getBoolean(1);
                break;
            case Byte:
                result = r.getByte(1);
                break;
            case Character:
                result = (char) r.getInt(1);
                break;
            case Date:
                result = r.getDate(1);
                break;
            case Double:
                result = r.getDouble(1);
                break;
            case Short:
                result = r.getShort(1);
                break;
            case Float:
                result = r.getFloat(1);
                break;
            case Integer:
                result = r.getInt(1);
                break;
            case Long:
                result = r.getLong(1);
                break;
            case SqlDate:
                result = r.getDate(1);
                break;
            case String:
                result = r.getString(1);
                break;
            case Timestamp:
                result = r.getTimestamp(1);
                break;
            default:
                throw new IllegalArgumentException("Unsupported type: " + type);
        }
        return r.wasNull() ? null : result;
    }

    @Override
    public String toString() {
        return "JavaTypeMapper[" + type + "]";
    }
}
