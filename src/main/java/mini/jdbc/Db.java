package mini.jdbc;

import org.jetbrains.annotations.NotNull;

/**
 *
 */
public interface Db {

    <T> T attach(@NotNull final Dbi impl, Class<T> interfaceClass);

    <T> T execute(@NotNull DbOp<T> op);
}
