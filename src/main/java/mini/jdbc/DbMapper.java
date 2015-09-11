package mini.jdbc;


import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

/**
 *
 */
public interface DbMapper<T> {
    @NotNull
    T map(DbResultSet r) throws SQLException;
}
