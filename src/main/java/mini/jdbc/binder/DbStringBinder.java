package mini.jdbc.binder;

import mini.jdbc.DbParameterBinder;
import mini.jdbc.binder.JavaTypeBinder.JavaType;
import mini.jdbc.type.DbString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DbStringBinder implements DbParameterBinder<DbString> {
    @Override
    public void bind(@NotNull PreparedStatement statement, int idx, @Nullable DbString value) throws SQLException {
        JavaTypeBinder.bind(statement, idx, value == null ? null : value.getStringValue(), JavaType.String);
    }
}
