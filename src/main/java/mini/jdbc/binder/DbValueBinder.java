package mini.jdbc.binder;

import mini.jdbc.DbBinder;
import mini.jdbc.type.DbInt;
import mini.jdbc.type.DbLong;
import mini.jdbc.type.DbString;
import mini.jdbc.util.JavaType;
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
            default:
                throw new IllegalArgumentException("Unsupported DbValue type: " + type);
        }
    }
}
