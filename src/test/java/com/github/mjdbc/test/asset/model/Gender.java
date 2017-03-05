package com.github.mjdbc.test.asset.model;

import com.github.mjdbc.type.DbInt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum Gender implements DbInt {
    MALE(0),
    FEMALE(1);

    private final int dbValue;

    Gender(int dbValue) {
        this.dbValue = dbValue;
    }

    @Override
    public int getDbValue() {
        return dbValue;
    }

    @Nullable
    public static Gender fromDbValue(int dbValue) {
        return dbValue == 0 ? MALE : dbValue == 1 ? FEMALE : null;
    }

    @NotNull
    public Gender opposite() {
        return this == MALE ? FEMALE : MALE;
    }
}
