package mini.jdbc;

import org.jetbrains.annotations.NotNull;

/**
 * Database operation with @NotNull result.
 */
public interface DbOpNN<T> {
    @NotNull
    T run(@NotNull DbConnection c) throws Exception;
}
