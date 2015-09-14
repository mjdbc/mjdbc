package mini.jdbc.binder;

import mini.jdbc.DbParameterBinder;
import mini.jdbc.binder.JavaTypeBinder.JavaType;
import mini.jdbc.type.DbLong;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DbLongBinder implements DbParameterBinder<DbLong> {
    @Override
    public void bind(@NotNull PreparedStatement statement, int idx, @Nullable DbLong value) throws SQLException {
        JavaTypeBinder.bind(statement, idx, value == null ? null : value.getLongValue(), JavaType.Long);
    }
}
