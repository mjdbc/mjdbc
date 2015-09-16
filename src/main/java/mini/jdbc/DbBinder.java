package mini.jdbc;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Sets statement's parameter value by the provided index.
 * See JavaTypeBinder or DbValueBinder for sample implementations.
 */
public interface DbBinder<T> {
    void bind(@NotNull PreparedStatement statement, int idx, @Nullable T value) throws SQLException;
}
