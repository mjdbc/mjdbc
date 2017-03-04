package com.github.mjdbc.test.asset.types;

import com.github.mjdbc.type.DbString;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractDbString implements DbString {

    @Override
    public int hashCode() {
        String val = getDbValue();
        return val == null ? 0 : val.hashCode();
    }

    @Override
    public boolean equals(@Nullable Object o) {
        return this == o || (o != null && o instanceof DbString && equals(getDbValue(), ((DbString) o).getDbValue()));
    }

    private static boolean equals(@Nullable String v1, @Nullable String v2) {
        //noinspection StringEquality
        return v1 == v2 || (v1 != null && v1.equals(v2));
    }
}
