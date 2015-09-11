package mini.jdbc;

import org.jetbrains.annotations.NotNull;

/**
 *
 */
public interface DbOpV {
    void run(@NotNull DbConnection c) throws Exception;
}
