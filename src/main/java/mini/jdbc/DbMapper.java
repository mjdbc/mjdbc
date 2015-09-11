package mini.jdbc;


import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 */
public interface DbMapper<T> {
    @NotNull
    T map(ResultSet r) throws SQLException;
}
