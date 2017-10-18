package com.github.mjdbc;

import javax.sql.DataSource;
import org.jetbrains.annotations.NotNull;

/**
 * Helper factory interface for mJDBC helpers.
 */
public class MJDBC {

    public static Db newDb(@NotNull DataSource dataSource) {
        return new DbImpl(dataSource);
    }

}
