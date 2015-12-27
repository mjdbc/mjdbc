package com.github.mjdbc.test;

import org.jetbrains.annotations.NotNull;

public abstract class BaseJavaTypesTest<S> extends BaseSqlTest<S> {
    public BaseJavaTypesTest(@NotNull Class<S> type) {
        super(type, "types");
    }
}
