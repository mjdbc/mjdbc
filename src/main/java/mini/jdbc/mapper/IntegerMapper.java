package mini.jdbc.mapper;

import mini.jdbc.DbMapper;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 */
public class IntegerMapper implements DbMapper<Integer> {
    public static final IntegerMapper INSTANCE = new IntegerMapper();

    @NotNull
    @Override
    public Integer map(ResultSet r) throws SQLException {
        return r.getInt(0);
    }
}
