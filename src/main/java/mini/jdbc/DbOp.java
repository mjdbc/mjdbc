package mini.jdbc;

import org.jetbrains.annotations.NotNull;

import java.sql.Connection;

/**
 *
 */
public interface DbOp<T> {
    //TODO: nullable?
    T run(@NotNull Connection c) throws Exception;
}
