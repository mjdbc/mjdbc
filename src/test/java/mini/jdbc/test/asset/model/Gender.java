package mini.jdbc.test.asset.model;

import mini.jdbc.type.DbInt;
import org.jetbrains.annotations.Nullable;

public enum Gender implements DbInt {
    MALE(0),
    FEMALE(1);

    private final int dbValue;

    Gender(int dbValue) {
        this.dbValue = dbValue;
    }

    @Override
    public int getIntValue() {
        return dbValue;
    }

    @Nullable
    public static Gender fromDbValue(int dbValue) {
        return dbValue == 0 ? MALE : dbValue == 1 ? FEMALE : null;
    }
}
