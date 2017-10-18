package com.github.mjdbc;


import java.sql.ResultSet;
import java.sql.SQLException;
import org.jetbrains.annotations.NotNull;

/**
 * Java object mapper for java.sql.ResultSet
 * Constructs Java object from ResultSet.
 */
public interface DbMapper<T> {

    /**
     * Maps a single row or all rows to the corresponding Java object.
     * May return null for nullable primitive type fields (like nullable varchar field).
     * Must never return null for complex java property objects (beans).
     *
     * @param r - open result set.
     * @return Java object.
     * @throws SQLException if SQLException occurs during the mapping process
     */
    T map(@NotNull ResultSet r) throws SQLException;
}
