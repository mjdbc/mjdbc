package mini.jdbc;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 */
public interface DbParameterBinder<T> {
    void bind(@NotNull PreparedStatement statement, int idx, @Nullable T value) throws SQLException;
}
