package mini.jdbc;


import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Java object mapper for java.sql.ResultSet
 * Constructs Java object from ResultSet.
 */
public interface DbMapper<T> {

    /**
     * Maps a single row or all rows to the corresponding Java object.
     *
     * @param r - open result set.
     * @return Java object.
     * @throws SQLException
     */
    @NotNull
    T map(ResultSet r) throws SQLException;
}
