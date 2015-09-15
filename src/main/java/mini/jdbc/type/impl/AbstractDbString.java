package mini.jdbc.type.impl;

import mini.jdbc.type.DbString;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractDbString implements DbString {

    @Override
    public int hashCode() {
        String val = getDbValue();
        return val == null ? 0 : val.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return this == o || !(o == null || getClass() != o.getClass()) && equals(getDbValue(), ((DbString) o).getDbValue());
    }

    private boolean equals(@Nullable String v1, @Nullable String v2) {
        //noinspection StringEquality
        return v1 == v2 || (v1 != null && v1.equals(v2));
    }
}
