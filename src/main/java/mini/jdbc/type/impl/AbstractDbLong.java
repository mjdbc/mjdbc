package mini.jdbc.type.impl;

import mini.jdbc.type.DbLong;

public abstract class AbstractDbLong implements DbLong {

    @Override
    public int hashCode() {
        return Long.hashCode(getDbValue());
    }

    @Override
    public boolean equals(Object o) {
        return this == o || !(o == null || getClass() != o.getClass()) && getDbValue() == ((DbLong) o).getDbValue();
    }
}
