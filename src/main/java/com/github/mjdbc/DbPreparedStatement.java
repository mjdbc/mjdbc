package com.github.mjdbc;

import com.github.mjdbc.DbImpl.BindInfo;
import com.github.mjdbc.type.DbInt;
import com.github.mjdbc.type.DbLong;
import com.github.mjdbc.type.DbString;
import com.github.mjdbc.type.DbTimestamp;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
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
import java.util.Objects;

/**
 * Wrapper over ${PreparedStatement} with named parameters, results mapping ({@link DbMapper})
 * and bean binding ({@link @{@link BindBean}}) support.
 * <p>
 * Provides full access to underlying ${PreparedStatement} and ads useful utility methods.
 * <p>
 * DbPreparedStatement is automatically closed on transaction commit/rollback.
 */
public class DbPreparedStatement<T> implements AutoCloseable {

    /**
     * Actual java.sql.PreparedStatement in use.
     */
    @NotNull
    public final PreparedStatement statement;

    /**
     * Mapper for the result. Used by methods like ${link {@link #query()} or {@link #queryList()}}
     */
    @NotNull
    public final DbMapper<T> resultMapper;

    /**
     * Mapping of parameter names to indexes.
     */
    @NotNull
    private final Map<String, List<Integer>> parametersMapping;

    @NotNull
    private final DbConnection dbConnection;

    public DbPreparedStatement(@NotNull DbConnection dbc, @NotNull String sql) throws SQLException {
        //noinspection unchecked
        this(dbc, sql, (DbMapper<T>) Mappers.VoidMapper, false);
    }

    public DbPreparedStatement(@NotNull DbConnection dbc, @NotNull String sql, @NotNull Class<T> resultClass) throws SQLException {
        this(dbc, sql, Objects.requireNonNull(DbImpl.findOrResolveMapperByType(resultClass, null)), false);
    }

    public DbPreparedStatement(@NotNull DbConnection dbc, @NotNull String sql, @NotNull DbMapper<T> resultMapper) throws SQLException {
        this(dbc, sql, resultMapper, false);
    }

    public DbPreparedStatement(@NotNull DbConnection dbc, @NotNull String sql, @NotNull DbMapper<T> resultMapper, boolean returnGeneratedKeys) throws SQLException {
        this.resultMapper = resultMapper;
        this.parametersMapping = new HashMap<>();
        this.dbConnection = dbc;
        String parsedSql = parse(sql, this.parametersMapping);
        statement = prepareStatement(dbc.getConnection(), parsedSql, returnGeneratedKeys);
        dbc.statementsToClose.add(this);
    }

    protected DbPreparedStatement(@NotNull DbConnection dbc, @NotNull String parsedSql, @NotNull DbMapper<T> resultMapper,
                                  @NotNull Map<String, List<Integer>> parametersMapping, boolean returnGeneratedKeys) throws SQLException {
        this.resultMapper = resultMapper;
        this.parametersMapping = parametersMapping;
        this.dbConnection = dbc;
        statement = prepareStatement(dbc.getConnection(), parsedSql, returnGeneratedKeys);
        dbc.statementsToClose.add(this);
    }

    @NotNull
    private static PreparedStatement prepareStatement(@NotNull Connection c, @NotNull String parsedSql, boolean returnGeneratedKeys) throws SQLException {
        return returnGeneratedKeys ? c.prepareStatement(parsedSql, Statement.RETURN_GENERATED_KEYS) : c.prepareStatement(parsedSql);
    }

    /**
     * Sets null for all fields matched by name.
     */
    @NotNull
    public DbPreparedStatement<T> setNull(@NotNull String name, @NotNull SQLType type) throws SQLException {
        for (int i : getIndexes(name)) {
            statement.setNull(i, type.getVendorTypeNumber());
        }
        return this;
    }

    /**
     * Sets boolean value for all fields matched by name.
     */
    @NotNull
    public DbPreparedStatement<T> set(@NotNull String name, boolean value) throws SQLException {
        for (int i : getIndexes(name)) {
            statement.setBoolean(i, value);
        }
        return this;
    }

    /**
     * Sets int value for all fields matched by name.
     */
    @NotNull
    public DbPreparedStatement<T> set(@NotNull String name, int value) throws SQLException {
        for (int i : getIndexes(name)) {
            statement.setInt(i, value);
        }
        return this;
    }

    /**
     * Sets int value for all fields matched by name. If value is null calls setNull for all fields.
     */
    @NotNull
    public DbPreparedStatement<T> set(@NotNull String name, @Nullable DbInt value) throws SQLException {
        return value == null ? setNull(name, JDBCType.INTEGER) : set(name, value.getDbValue());
    }

    /**
     * Sets long value for all fields matched by name.
     */
    @NotNull
    public DbPreparedStatement<T> set(@NotNull String name, long value) throws SQLException {
        for (int i : getIndexes(name)) {
            statement.setLong(i, value);
        }
        return this;
    }

    /**
     * Sets long value for all fields matched by name. If value is null calls setNull for all fields.
     */
    @NotNull
    public DbPreparedStatement<T> set(@NotNull String name, @Nullable DbLong value) throws SQLException {
        return value == null ? setNull(name, JDBCType.BIGINT) : set(name, value.getDbValue());
    }

    /**
     * Sets float value for all fields matched by name.
     */
    @NotNull
    public DbPreparedStatement<T> set(@NotNull String name, float value) throws SQLException {
        for (int i : getIndexes(name)) {
            statement.setFloat(i, value);
        }
        return this;
    }

    /**
     * Sets double value for all fields matched by name.
     */
    @NotNull
    public DbPreparedStatement<T> set(@NotNull String name, double value) throws SQLException {
        for (int i : getIndexes(name)) {
            statement.setDouble(i, value);
        }
        return this;
    }

    /**
     * Sets BigDecimal value for all fields matched by name.
     */
    @NotNull
    public DbPreparedStatement<T> set(@NotNull String name, @Nullable BigDecimal value) throws SQLException {
        for (int i : getIndexes(name)) {
            statement.setBigDecimal(i, value);
        }
        return this;
    }

    /**
     * Sets string value for all fields matched by name.
     */
    @NotNull
    public DbPreparedStatement<T> set(@NotNull String name, @Nullable String value) throws SQLException {
        for (int i : getIndexes(name)) {
            statement.setString(i, value);
        }
        return this;
    }

    /**
     * Sets string value for all fields matched by name. If value is null -> sets null.
     */
    @NotNull
    public DbPreparedStatement<T> set(@NotNull String name, @Nullable DbString value) throws SQLException {
        return set(name, value == null ? null : value.getDbValue());
    }

    /**
     * Sets byte[] value for all fields matched by name.
     */
    @NotNull
    public DbPreparedStatement<T> set(@NotNull String name, @Nullable byte[] value) throws SQLException {
        for (int i : getIndexes(name)) {
            statement.setBytes(i, value);
        }
        return this;
    }

    /**
     * Sets Timestamp value for all fields matched by name.
     */
    @NotNull
    public DbPreparedStatement<T> set(@NotNull String name, @Nullable Timestamp value) throws SQLException {
        for (int i : getIndexes(name)) {
            statement.setTimestamp(i, value);
        }
        return this;
    }

    /**
     * Sets Timestamp value for all fields matched by name. If value is null -> sets null.
     */
    @NotNull
    public DbPreparedStatement<T> set(@NotNull String name, @Nullable DbTimestamp value) throws SQLException {
        return set(name, value == null ? null : value.getDbValue());
    }

    /**
     * Sets all bean properties to named parameters.
     *
     * @param bean bean to map to named SQL parameters.
     * @return this.
     * @throws SQLException if anything bad happens during SQL operations or bean field accessors calls.
     */
    public DbPreparedStatement<T> bindBean(@NotNull Object bean) throws SQLException {
        return bindBean(dbConnection.db, bean, true);
    }

    /**
     * Sets all bean properties to named parameters.
     *
     * @param db                database to use. This method call relies on binders registered in this database instance.
     * @param bean              bean to map to named SQL parameters.
     * @param allowCustomFields if false and SQL contains keys not resolved with this bean -> SQLException will be thrown.
     * @return this.
     * @throws SQLException if anything bad happens during SQL operations or bean field accessors calls.
     */
    @NotNull
    public DbPreparedStatement<T> bindBean(@NotNull Db db, @NotNull Object bean, boolean allowCustomFields) throws SQLException {
        List<BindInfo> binders = ((DbImpl) db).getBeanBinders(bean.getClass());
        for (String key : parametersMapping.keySet()) {
            BindInfo info = binders.stream().filter(i -> i.mappedName.equals(key)).findFirst().orElse(null);
            if (info == null) {
                if (allowCustomFields) {
                    continue;
                }
                throw new SQLException("No mapping found for field: " + key);
            }
            try {
                bindArg(this, info, getIndexes(key), bean);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new SQLException("Error applying bean properties: " + bean, e);
            }
        }
        return this;
    }

    protected static void bindArg(@NotNull DbPreparedStatement s, @NotNull DbImpl.BindInfo bi, @NotNull List<Integer> indexes, @NotNull Object bean) throws IllegalAccessException, InvocationTargetException, SQLException {
        for (Integer idx : indexes) {
            Object value = bi.field != null ? bi.field.get(bean) : bi.getter != null ? bi.getter.invoke(bean) : bean;
            //noinspection unchecked
            bi.binder.bind(s.statement, idx, value);
        }
    }


    /**
     * Executes sql statement and returns java.sql.ResultSet.
     */
    @NotNull
    public ResultSet executeQuery() throws SQLException {
        return statement.executeQuery();
    }

    /**
     * Executes sql statement and returns first result mapped using {@link #resultMapper}.
     */
    @Nullable
    public T query() throws SQLException {
        try (ResultSet r = executeQuery()) {
            if (r.next()) {
                return resultMapper.map(r);
            }
            return null;
        }
    }

    /**
     * Same as {@link #query()} but checks if result value is not null.
     * Throws {@link java.lang.NullPointerException} if value is null.
     */
    @NotNull
    public T queryNN() throws SQLException {
        T res = null;
        try (ResultSet r = statement.executeQuery()) {
            if (r.next()) {
                res = resultMapper.map(r);
            }
            Objects.requireNonNull(res);
            return res;
        }
    }

    /**
     * Executes sql statement and returns list of all values in result set mapped using {@link #resultMapper}.
     */
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

    /**
     * Executes insert method on wrapped statement.
     */
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

    /**
     * Runs {@link PreparedStatement#executeUpdate()} on wrapped statement and returns it's result.
     */
    public int update() throws SQLException {
        return statement.executeUpdate();
    }

    /**
     * Returns list of indexes for a given key.
     * If key is not found throws {@link IllegalArgumentException}
     */
    @NotNull
    public List<Integer> getIndexes(String name) {
        List<Integer> indexes = parametersMapping.get(name);
        if (indexes == null) {
            throw new IllegalArgumentException("Parameter not found: " + name);
        }
        return indexes;
    }


    /**
     * Closes underlying sql statement.
     */
    @Override
    public void close() throws SQLException {
        statement.close();
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
                        if (c == ':' && i + 1 < length && Character.isJavaIdentifierStart(sql.charAt(i + 1))) {
                            int j = i + 2;
                            while (j < length && Character.isJavaIdentifierPart(sql.charAt(j))) {
                                j++;
                            }
                            String name = sql.substring(i + 1, j);
                            c = '?'; // replace the parameter with a question mark
                            i += name.length(); // skip past the end if the parameter

                            List<Integer> indexList = paramMap.computeIfAbsent(name, k -> new ArrayList<>());
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
    public String toString() {
        return "DbPS[" + statement + "]";
    }
}