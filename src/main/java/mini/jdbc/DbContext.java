package mini.jdbc;

import org.jetbrains.annotations.NotNull;

import java.sql.Connection;

/**
 *
 */
public class DbContext {
    @NotNull
    final Db db;
    Connection connection;

    DbContext(@NotNull Db db) {
        this.db = db;
    }

    public <T> T execute(@NotNull DbOp<T> op) {
        return db.execute(op, this);
    }

    @NotNull
    public Db getDb() {
        return db;
    }

    public Connection getConnection() {
        return connection;
    }
}
