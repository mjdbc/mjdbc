package mini.jdbc.binder;

import mini.jdbc.DbBinder;
import mini.jdbc.util.JavaType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;

public class JavaTypeBinder implements DbBinder {

    private final JavaType type;

    public JavaTypeBinder(@NotNull JavaType type) {
        this.type = type;
    }

    @Override
    public void bind(@NotNull PreparedStatement statement, int idx, @Nullable Object value) throws SQLException {
        bind(statement, idx, value, type);
    }

    public static void bind(@NotNull PreparedStatement statement, int idx, @Nullable Object value, @NotNull JavaType type) throws SQLException {
        if (value == null) {
            bindNull(statement, idx, type);
            return;
        }
        switch (type) {
            case BigDecimal:
                statement.setBigDecimal(idx, (BigDecimal) value);
                break;
            case Boolean:
                statement.setBoolean(idx, (Boolean) value);
                break;
            case Byte:
                statement.setByte(idx, (Byte) value);
                break;
            case Character:
                statement.setInt(idx, (Character) value);
                break;
            case Date:
                statement.setDate(idx, new java.sql.Date(((Date) value).getTime()));
                break;
            case Double:
                statement.setDouble(idx, (Double) value);
                break;
            case Short:
                statement.setShort(idx, (Short) value);
                break;
            case Float:
                statement.setFloat(idx, (Float) value);
                break;
            case Integer:
                statement.setInt(idx, (Integer) value);
                break;
            case Long:
                statement.setLong(idx, (Long) value);
                break;
            case SqlDate:
                statement.setDate(idx, (java.sql.Date) value);
                break;
            case String:
                statement.setString(idx, value.toString());
                break;
            case Timestamp:
                statement.setTimestamp(idx, (Timestamp) value);
                break;
            default:
                throw new IllegalArgumentException("Illegal type: " + type);
        }
    }

    public static void bindNull(PreparedStatement statement, int idx, @NotNull JavaType type) throws SQLException {
        switch (type) {
            case BigDecimal:
                statement.setNull(idx, Types.DECIMAL);
                break;
            case Boolean:
                statement.setNull(idx, Types.BOOLEAN);
                break;
            case Byte:
                statement.setNull(idx, Types.INTEGER);
                break;
            case Character:
                statement.setNull(idx, Types.INTEGER);
                break;
            case Date:
                statement.setNull(idx, Types.DATE);
                break;
            case Double:
                statement.setNull(idx, Types.DOUBLE);
                break;
            case Short:
                statement.setNull(idx, Types.INTEGER);
                break;
            case Float:
                statement.setNull(idx, Types.FLOAT);
                break;
            case Integer:
                statement.setNull(idx, Types.INTEGER);
                break;
            case Long:
                statement.setNull(idx, Types.INTEGER);
                break;
            case SqlDate:
                statement.setNull(idx, Types.DATE);
                break;
            case String:
                statement.setString(idx, null);
                break;
            case Timestamp:
                statement.setNull(idx, Types.TIMESTAMP);
                break;
        }
    }

}
