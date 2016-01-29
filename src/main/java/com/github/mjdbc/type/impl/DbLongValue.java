package com.github.mjdbc.type.impl;

public class DbLongValue extends AbstractDbLong {
    protected long value;

    public DbLongValue(int value) {
        this.value = value;
    }

    public final long getDbValue() {
        return value;
    }

    public String toString() {
        return getClass().getSimpleName() + "[" + value + "]";
    }
}
