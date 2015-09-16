package com.github.mjdbc;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Database operation with @Nullable result.
 */
public interface DbOp<T> {
    @Nullable
    T run(@NotNull DbConnection c) throws Exception;
}
