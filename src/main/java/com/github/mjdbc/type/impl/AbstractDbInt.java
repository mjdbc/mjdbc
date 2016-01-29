package com.github.mjdbc.type.impl;

import com.github.mjdbc.type.DbInt;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractDbInt implements DbInt {

    @Override
    public int hashCode() {
        return getDbValue();
    }

    @Override
    public boolean equals(@Nullable Object o) {
        return this == o || (o != null && o instanceof DbInt && getDbValue() == ((DbInt) o).getDbValue());
    }
}
