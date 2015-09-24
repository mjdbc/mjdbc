package com.github.mjdbc.binder;

import com.github.mjdbc.DbBinder;
import com.github.mjdbc.type.DbInt;
import com.github.mjdbc.type.DbLong;
import com.github.mjdbc.type.DbString;
import com.github.mjdbc.type.DbTimestamp;
import com.github.mjdbc.util.JavaType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DbValueBinder implements DbBinder {
    @NotNull
    private final JavaType type;

    public DbValueBinder(@NotNull JavaType type) {
        this.type = type;
    }

    @Override
    public void bind(@NotNull PreparedStatement statement, int idx, @Nullable Object value) throws SQLException {
        switch (type) {
            case Integer:
                JavaTypeBinder.bind(statement, idx, value == null ? null : ((DbInt) value).getDbValue(), JavaType.Integer);
                break;
            case Long:
                JavaTypeBinder.bind(statement, idx, value == null ? null : ((DbLong) value).getDbValue(), JavaType.Long);
                break;
            case String:
                JavaTypeBinder.bind(statement, idx, value == null ? null : ((DbString) value).getDbValue(), JavaType.String);
                break;
            case Timestamp:
                JavaTypeBinder.bind(statement, idx, value == null ? null : ((DbTimestamp) value).getDbValue(), JavaType.Timestamp);
                break;
            default:
                throw new IllegalArgumentException("Unsupported DbValue type: " + type);
        }
    }
}
