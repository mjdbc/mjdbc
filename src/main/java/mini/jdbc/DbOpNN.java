package mini.jdbc;

import org.jetbrains.annotations.NotNull;

import java.sql.Connection;

/**
 *
 */
public interface DbOpNN<T> {
    @NotNull
    T run(@NotNull DbConnection c) throws Exception;
}
