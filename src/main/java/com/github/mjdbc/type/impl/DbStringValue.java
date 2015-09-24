package com.github.mjdbc.type.impl;

public class DbStringValue extends AbstractDbString {
    protected String value;

    public DbStringValue() {
    }

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
