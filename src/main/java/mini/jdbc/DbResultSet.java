package mini.jdbc;

import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 */
public final class DbResultSet implements AutoCloseable {

    @NotNull
    public ResultSet resultSet;

    public DbResultSet(@NotNull ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    public boolean getBoolean(@NotNull String key) throws SQLException {
        return resultSet.getBoolean(key);
    }

    public int getInt(@NotNull String key) throws SQLException {
        return resultSet.getInt(key);
    }

    public int getInt(int idx) throws SQLException {
        return resultSet.getInt(idx);
    }

    public long getLong(@NotNull String key) throws SQLException {
        return resultSet.getLong(key);
    }

    public String getString(@NotNull String key) throws SQLException {
        return resultSet.getString(key);
    }

    public String getString(int idx) throws SQLException {
        return resultSet.getString(idx);
    }

    @Override
    public void close() throws SQLException {
        resultSet.close();
    }

    public boolean next() throws SQLException {
        return resultSet.next();
    }
}
