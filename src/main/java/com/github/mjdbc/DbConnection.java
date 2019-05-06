package com.github.mjdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Lightweight java.sql.Connection wrapper.
 * Used in pair with DbPreparedStatement and DbOpX classes for automatic statements cleanup when connection is closed.
 */
public class DbConnection {

    @NotNull
    protected final DbImpl db;

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

    protected DbConnection(@NotNull DbImpl db) {
        this.db = db;
    }

    @NotNull
    public Connection getConnection() {
        return ensureConnection();
    }

    @NotNull
    public Statement createStatement() throws SQLException {
        return getConnection().createStatement();
    }

    @NotNull
    public DbPreparedStatement prepareStatement(@NotNull String sql) throws SQLException {
        return new DbPreparedStatement(this, sql);
    }

    @NotNull
    public <T> DbPreparedStatement<T> prepareStatement(@NotNull String sql, @NotNull Class<T> resultClass) throws SQLException {
        return new DbPreparedStatement<>(this, sql, resultClass);
    }

    @NotNull
    public <T> DbPreparedStatement<T> prepareStatement(@NotNull String sql, @NotNull DbMapper<T> resultMapper) throws SQLException {
        return new DbPreparedStatement<>(this, sql, resultMapper);
    }

    @NotNull
    public <T> DbPreparedStatement<T> prepareStatement(@NotNull String sql, @NotNull DbMapper<T> resultMapper, boolean returnGeneratedKeys) throws SQLException {
        return new DbPreparedStatement<>(this, sql, resultMapper, returnGeneratedKeys);
    }

    @NotNull
    public <T> DbPreparedStatement<T> prepareStatement(@NotNull String sql, @NotNull DbMapper<T> resultMapper, @NotNull Map<String, List<Integer>> parametersMapping, boolean returnGeneratedKeys) throws SQLException {
        return new DbPreparedStatement<>(this, sql, resultMapper, parametersMapping, returnGeneratedKeys);
    }

    @NotNull
    private Connection ensureConnection() {
        if (sqlConnection == null) {
            try {
                sqlConnection = db.dataSource.getConnection();
            } catch (SQLException e) {
                throw new RuntimeException("Failed to get connection from DataSource!", e);
            }
        }
        return sqlConnection;
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
