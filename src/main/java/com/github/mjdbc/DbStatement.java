package com.github.mjdbc;

import com.github.mjdbc.type.DbInt;
import com.github.mjdbc.type.DbLong;
import com.github.mjdbc.type.DbString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.JDBCType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLType;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Statement with named parameters support and safe operations to fetch results by hiding result set manipulation.
 */
public class DbStatement<T> implements AutoCloseable {
    @NotNull
    public final PreparedStatement statement;

    @NotNull
    public final DbMapper<T> resultMapper;

    private final Map<String, List<Integer>> parametersMapping;

    public DbStatement(@NotNull DbConnection c, @NotNull String sql, @NotNull DbMapper<T> resultMapper) throws SQLException {
        this(c, sql, resultMapper, false);
    }

    public DbStatement(@NotNull DbConnection c, @NotNull String sql, @NotNull DbMapper<T> resultMapper, boolean useGeneratedKeys) throws SQLException {
        this.resultMapper = resultMapper;
        this.parametersMapping = new HashMap<>();
        String parsedSql = parse(sql, this.parametersMapping);
        statement = prepareStatement(c.sqlConnection, parsedSql, useGeneratedKeys);
        c.statementsToClose.add(this);
    }

    public DbStatement(@NotNull DbConnection c, @NotNull String parsedSql, @NotNull DbMapper<T> resultMapper,
                       Map<String, List<Integer>> parametersMapping, boolean useGeneratedKeys) throws SQLException {
        this.resultMapper = resultMapper;
        this.parametersMapping = parametersMapping;
        statement = prepareStatement(c.sqlConnection, parsedSql, useGeneratedKeys);
        c.statementsToClose.add(this);
    }

    private PreparedStatement prepareStatement(@NotNull Connection c, @NotNull String parsedSql, boolean useGeneratedKeys) throws SQLException {
        return useGeneratedKeys ? c.prepareStatement(parsedSql, Statement.RETURN_GENERATED_KEYS) : c.prepareStatement(parsedSql);
    }


    public DbStatement<T> setNull(@NotNull String name, @NotNull SQLType type) throws SQLException {
        for (int i : getIndexes(name)) {
            statement.setNull(i, type.getVendorTypeNumber());
        }
        return this;
    }

    @NotNull
    public DbStatement<T> setBoolean(@NotNull String name, boolean value) throws SQLException {
        for (int i : getIndexes(name)) {
            statement.setBoolean(i, value);
        }
        return this;
    }

    @NotNull
    public DbStatement<T> setInt(@NotNull String name, int value) throws SQLException {
        for (int i : getIndexes(name)) {
            statement.setInt(i, value);
        }
        return this;
    }

    @NotNull
    public DbStatement<T> setInt(@NotNull String name, @Nullable DbInt value) throws SQLException {
        return value == null ? setNull(name, JDBCType.INTEGER) : setInt(name, value.getDbValue());
    }

    @NotNull
    public DbStatement<T> setLong(@NotNull String name, long value) throws SQLException {
        for (int i : getIndexes(name)) {
            statement.setLong(i, value);
        }
        return this;
    }

    @NotNull
    public DbStatement<T> setLong(@NotNull String name, @Nullable DbLong value) throws SQLException {
        return value == null ? setNull(name, JDBCType.BIGINT) : setLong(name, value.getDbValue());
    }

    @NotNull
    public DbStatement<T> setFloat(@NotNull String name, float value) throws SQLException {
        for (int i : getIndexes(name)) {
            statement.setFloat(i, value);
        }
        return this;
    }

    @NotNull
    public DbStatement<T> setDouble(@NotNull String name, double value) throws SQLException {
        for (int i : getIndexes(name)) {
            statement.setDouble(i, value);
        }
        return this;
    }

    @NotNull
    public DbStatement<T> setBigDecimal(@NotNull String name, BigDecimal value) throws SQLException {
        for (int i : getIndexes(name)) {
            statement.setBigDecimal(i, value);
        }
        return this;
    }

    @NotNull
    public DbStatement<T> setString(@NotNull String name, String value) throws SQLException {
        for (int i : getIndexes(name)) {
            statement.setString(i, value);
        }
        return this;
    }

    @NotNull
    public DbStatement<T> setString(@NotNull String name, @Nullable DbString value) throws SQLException {
        return setString(name, value == null ? null : value.getDbValue());
    }

    @NotNull
    public DbStatement<T> setBytes(@NotNull String name, byte[] value) throws SQLException {
        for (int i : getIndexes(name)) {
            statement.setBytes(i, value);
        }
        return this;
    }

    @NotNull
    public DbStatement<T> setTimestamp(@NotNull String name, Timestamp value) throws SQLException {
        for (int i : getIndexes(name)) {
            statement.setTimestamp(i, value);
        }
        return this;
    }

    @NotNull
    public ResultSet executeQuery() throws SQLException {
        return statement.executeQuery();
    }


    @Nullable
    public T query() throws SQLException {
        try (ResultSet r = statement.executeQuery()) {
            if (r.next()) {
                return resultMapper.map(r);
            }
            return null;
        }
    }

    @NotNull
    public List<T> queryList() throws SQLException {
        List<T> res = new ArrayList<>();
        try (ResultSet r = statement.executeQuery()) {
            while (r.next()) {
                res.add(resultMapper.map(r));
            }
        }
        return res;
    }


    public int insert() throws SQLException {
        return statement.executeUpdate();
    }

    @NotNull
    public T updateAndGetGeneratedKeys() throws SQLException {
        statement.executeUpdate();
        try (ResultSet r = statement.getGeneratedKeys()) {
            if (r.next()) {
                return resultMapper.map(r);
            }
            throw new SQLException("Result set is empty!");
        }
    }

    public int executeUpdate() throws SQLException {
        return statement.executeUpdate();
    }

    @NotNull
    public List<Integer> getIndexes(String name) {
        List<Integer> indexes = parametersMapping.get(name);
        if (indexes == null) {
            throw new IllegalArgumentException("Parameter not found: " + name);
        }
        return indexes;
    }


    // TODO: add mappings cache for parsed maps -> reuse DbConnection -> Db::cache!
    @NotNull
    protected static String parse(@NotNull String sql, @NotNull Map<String, List<Integer>> paramMap) {
        int length = sql.length();
        StringBuilder parsedQuery = new StringBuilder(length);
        boolean inSingleQuote = false;
        boolean inDoubleQuote = false;
        int index = 1;

        for (int i = 0; i < length; i++) {
            char c = sql.charAt(i);
            if (inSingleQuote) {
                if (c == '\'') {
                    inSingleQuote = false;
                }
            } else if (inDoubleQuote) {
                if (c == '"') {
                    inDoubleQuote = false;
                }
            } else {
                if (c == '\'') {
                    inSingleQuote = true;
                } else {
                    if (c == '"') {
                        inDoubleQuote = true;
                    } else {
                        if (c == ':' && i + 1 < length &&
                                Character.isJavaIdentifierStart(sql.charAt(i + 1))) {
                            int j = i + 2;
                            while (j < length && Character.isJavaIdentifierPart(sql.charAt(j))) {
                                j++;
                            }
                            String name = sql.substring(i + 1, j);
                            c = '?'; // replace the parameter with a question mark
                            i += name.length(); // skip past the end if the parameter

                            List<Integer> indexList = paramMap.get(name);
                            if (indexList == null) {
                                indexList = new ArrayList<>();
                                paramMap.put(name, indexList);
                            }
                            indexList.add(index);

                            index++;
                        }
                    }
                }
            }
            parsedQuery.append(c);
        }
        return parsedQuery.toString();
    }

    @Override
    public void close() throws SQLException {
        statement.close();
    }
}