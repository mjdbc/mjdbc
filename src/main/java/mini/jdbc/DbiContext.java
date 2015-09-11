package mini.jdbc;

import org.jetbrains.annotations.NotNull;

/**
 *
 */
public class DbiContext {
    @NotNull
    private final DbImpl db;

    DbiContext(@NotNull DbImpl db) {
        this.db = db;
    }

    public <T> T execute(@NotNull DbOp<T> op) {
        return db.execute(op);
    }

    @NotNull
    public Db getDb() {
        return db;
    }
}
