package mini.jdbc;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;

/**
 *
 */
public interface DbOp<T> {
    @Nullable
    T run(@NotNull DbConnection c) throws Exception;
}