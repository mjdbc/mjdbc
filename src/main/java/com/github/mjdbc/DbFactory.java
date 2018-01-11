package com.github.mjdbc;

import java.util.EnumSet;
import javax.sql.DataSource;
import org.jetbrains.annotations.NotNull;

/**
 * Helper factory interface for mJDBC helpers.
 */
public class DbFactory {

    /**
     * Creates new mjdbc Db wrapper for the given datasource.
     */
    @NotNull
    public static Db wrap(@NotNull DataSource dataSource) {
        return wrap(dataSource, EnumSet.noneOf(DbFlags.class));
    }

    @NotNull
    public static Db wrap(@NotNull DataSource dataSource, @NotNull EnumSet<DbFlags> flags) {
        return new DbImpl(dataSource, flags);
    }

}
