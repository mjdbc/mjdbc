package mini.jdbc.util;

import mini.jdbc.DbContext;
import mini.jdbc.Dbi;

import java.io.Closeable;

public abstract class AbstractDbi implements Dbi {

    protected DbContext context;

    @Override
    public void setContext(DbContext context) {
        this.context = context;
    }

    @Override
    public DbContext getContext() {
        return context;
    }
}
