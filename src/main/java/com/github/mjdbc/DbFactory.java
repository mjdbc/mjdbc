package com.github.mjdbc;

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
        return new DbImpl(dataSource);
    }

}
