package mini.jdbc;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 *
 */
public interface Db {

    <T> void registerMapper(@NotNull Class<T> mapperClass, @NotNull DbMapper<T> mapper);

    @NotNull
    <T> T attachDbi(@NotNull T impl, @NotNull Class<T> daoInterface);

    @NotNull
    <T> T attachQueries(@NotNull Class<T> queryInterface);

    @Nullable
    <T> T execute(@NotNull DbOp<T> op);

    @NotNull
    <T> T executeNN(@NotNull DbOpNN<T> op);

    void executeV(@NotNull DbOpV op);
}
