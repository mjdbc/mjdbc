package com.github.mjdbc;

import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Lightweight java.sql.Connection wrapper.
 * Used in pair with DbStatement and DbOpX classes for automatic statements cleanup when connection is closed.
 */
public class DbConnection {
    /**
     * Native SQL connection.
     */
    public final Connection sqlConnection;

    /**
     * List of statements to close when connection is closed.
     */
    @NotNull
    protected final List<DbStatement> statementsToClose = new ArrayList<>();

    public DbConnection(@NotNull Connection sqlConnection) {
        this.sqlConnection = sqlConnection;
    }

    public void commit() throws SQLException {
        sqlConnection.commit();
    }

    public void rollback() throws SQLException {
        sqlConnection.rollback();
    }

    public void close() throws SQLException {
        for (DbStatement s : statementsToClose) {
            try {
                s.close();
            } catch (SQLException ignored) {
            }
        }
        sqlConnection.close();
    }
}
