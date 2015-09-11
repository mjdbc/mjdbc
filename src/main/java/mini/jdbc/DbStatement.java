package mini.jdbc;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Statement that uses named parameters and safe operations to fetch results by hiding result set manipulation.
 */
public class DbStatement<T> implements AutoCloseable {
    @NotNull
    private final PreparedStatement statement;

    private final Map<String, List<Integer>> indexMap = new HashMap<>();
    private final DbMapper<T> mapper;

    public DbStatement(DbConnection c, String query, DbMapper<T> mapper) throws SQLException {
        this.mapper = mapper;
        String parsedQuery = parse(query, indexMap);
        try {
            statement = c.sqlConnection.prepareStatement(parsedQuery);
        } finally {
            c.statementsToClose.add(this);
        }
    }


    public DbStatement<T> setString(String name, String value) throws SQLException {
        for (int i : getIndexes(name)) {
            statement.setString(i, value);
        }
        return this;
    }


    public DbStatement<T> setInt(String name, int value) throws SQLException {
        for (int i : getIndexes(name)) {
            statement.setInt(i, value);
        }
        return this;
    }

    public DbStatement<T> setLong(String name, long value) throws SQLException {
        for (int i : getIndexes(name)) {
            statement.setLong(i, value);
        }
        return this;
    }


    public DbStatement<T> setBoolean(String name, boolean value) throws SQLException {
        for (int i : getIndexes(name)) {
            statement.setBoolean(i, value);
        }
        return this;
    }


    @Nullable
    public T query() throws SQLException {
        try (ResultSet r = statement.executeQuery()) {
            if (r.next()) {
                return mapper.map(r);
            }
            return null;
        }
    }

    @NotNull
    public List<T> queryList() throws SQLException {
        List<T> res = new ArrayList<>();
        try (ResultSet r = statement.executeQuery()) {
            while (r.next()) {
                res.add(mapper.map(r));
            }
        }
        return res;
    }


    public void insert() throws SQLException {
        statement.executeUpdate();
    }

    @NotNull
    public T insertAndReturnId() throws SQLException {
        try (ResultSet r = statement.executeQuery()) {
            if (r.next()) {
                return mapper.map(r);
            }
            throw new SQLException("Insert returned no ID!");
        }
    }

    public int update() throws SQLException {
        return statement.executeUpdate();
    }


    @NotNull
    private List<Integer> getIndexes(String name) {
        List<Integer> indexes = indexMap.get(name);
        if (indexes == null) {
            throw new IllegalArgumentException("Parameter not found: " + name);
        }
        return indexes;
    }


    // TODO: add mappings cache for parsed maps
    private static String parse(String query, Map<String, List<Integer>> paramMap) {
        int length = query.length();
        StringBuilder parsedQuery = new StringBuilder(length);
        boolean inSingleQuote = false;
        boolean inDoubleQuote = false;
        int index = 1;

        for (int i = 0; i < length; i++) {
            char c = query.charAt(i);
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
                                Character.isJavaIdentifierStart(query.charAt(i + 1))) {
                            int j = i + 2;
                            while (j < length && Character.isJavaIdentifierPart(query.charAt(j))) {
                                j++;
                            }
                            String name = query.substring(i + 1, j);
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