package com.github.mjdbc.test.asset.types;

public class DbIntValue extends AbstractDbInt {
    protected int value;

    public DbIntValue(int value) {
        this.value = value;
    }

    public final int getDbValue() {
        return value;
    }

    public String toString() {
        return getClass().getSimpleName() + "[" + value + "]";
    }
}
