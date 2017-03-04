package com.github.mjdbc.test.asset.types;

public class DbStringValue extends AbstractDbString {
    protected String value;

    public DbStringValue(String value) {
        this.value = value;
    }

    public final String getDbValue() {
        return value;
    }

    public String toString() {
        return getClass().getSimpleName() + "[" + value + "]";
    }
}
