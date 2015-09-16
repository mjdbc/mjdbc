package com.github.mjdbc.type.impl;

import com.github.mjdbc.type.DbInt;

public abstract class AbstractDbInt implements DbInt {

    @Override
    public int hashCode() {
        return getDbValue();
    }

    @Override
    public boolean equals(Object o) {
        return this == o || !(o == null || getClass() != o.getClass()) && getDbValue() == ((DbInt) o).getDbValue();

    }
}