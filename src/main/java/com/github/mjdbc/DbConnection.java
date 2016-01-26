package com.github.mjdbc;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Lightweight java.sql.Connection wrapper.
 * Used in pair with DbPreparedStatement and DbOpX classes for automatic statements cleanup when connection is closed.
 */
public class DbConnection {
    /**
     * Datasource to obtain sql connection
     */
    @Nullable
    private final DataSource dataSource;

    /**
     * Native SQL connection.
     */
    @Nullable
    private Connection sqlConnection;

    /**
     * List of statements to close when connection is closed.
     */
    @NotNull
    protected final List<DbPreparedStatement> statementsToClose = new ArrayList<>();

    public DbConnection(@NotNull DataSource dataSource) {
        this.dataSource = Objects.requireNonNull(dataSource);
    }

    public DbConnection(@NotNull Connection sqlConnection) {
        this.dataSource = null;
        this.sqlConnection = Objects.requireNonNull(sqlConnection);
    }

    @NotNull
    public Connection getConnection() {
        ensureConnection();
        assert sqlConnection != null;
        return sqlConnection;
    }

    private void ensureConnection() {
        if (sqlConnection == null) {
            assert dataSource != null;
            try {
                sqlConnection = dataSource.getConnection();
            } catch (SQLException e) {
                throw new RuntimeException("Failed to get connection from DataSource!", e);
            }
        }
    }

    public void commit() throws SQLException {
        // commit only if connection was opened.
        if (sqlConnection != null) {
            sqlConnection.commit();
        }
    }

    public void rollback() throws SQLException {
        // rollback only if connection was opened.
        if (sqlConnection != null) {
            sqlConnection.rollback();
        }
    }

    public void close() throws SQLException {
        for (DbPreparedStatement s : statementsToClose) {
            try {
                s.close();
            } catch (SQLException ignored) {
            }
        }
        if (sqlConnection != null) {
            sqlConnection.close();
        }
    }
}
