package com.github.mjdbc.type.impl;

import com.github.mjdbc.type.DbLong;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractDbLong implements DbLong {

    @Override
    public int hashCode() {
        return Long.hashCode(getDbValue());
    }

    @Override
    public boolean equals(@Nullable Object o) {
        return this == o || (o != null && o instanceof DbLong && getDbValue() == ((DbLong) o).getDbValue());
    }
}
