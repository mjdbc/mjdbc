package mini.jdbc;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Database access interface. Allows to register result set mappers, statement parameter binders and execute queries and updates over database.
 */
public interface Db {

    /**
     * Registers result set mapper. See DbMapper class for details.
     *
     * @param mapperClass - the result classes. Result set row will be mapped to the instance of this class.
     * @param mapper      - mapper. This mapper will be used to map result set row to mapperClass instance.
     * @param <T>         - result type.
     */
    <T> void registerMapper(@NotNull Class<T> mapperClass, @NotNull DbMapper<T> mapper);

    /**
     * Registers prepared statement parameter binder.
     * A binder for parameter is looked up by matching class first, any superclass second or any interface third.
     *
     * @param binderClass - parameter class to be processed by this binder.
     * @param binder      - binder implementation.
     */
    void registerBinder(@NotNull Class binderClass, @NotNull DbBinder binder);

    /**
     * Attaches Dbi (Database Interface) implementation to the database.
     * All methods marked with @Tx annotation will be wrapped to create transaction on entry and close it on exit.
     *
     * @param impl         - database interface implementation.
     * @param dbiInterface - database interface to wrap with transactions support.
     * @param <T>          - type of the Dbi interface.
     * @return wrapped instance of the @dbiInterface
     */
    @NotNull
    <T> T attachDbi(@NotNull T impl, @NotNull Class<T> dbiInterface);

    /**
     * Attaches query interface to the database. All queries are parsed and validated during this call.
     *
     * @param queryInterface - query interface to process.
     * @param <T>            - type of the interface.
     * @return interface implementation.
     */
    @NotNull
    <T> T attachQueries(@NotNull Class<T> queryInterface);

    /**
     * Executes database query that produces Nullable result.
     *
     * @param op  - query impl.
     * @param <T> - result type.
     * @return query result.
     */
    @Nullable
    <T> T execute(@NotNull DbOp<T> op);

    /**
     * Executes database query that produces NotNull result.
     *
     * @param op  - query impl.
     * @param <T> - result type.
     * @return query result.
     */
    @NotNull
    <T> T executeNN(@NotNull DbOpNN<T> op);

    /**
     * Executes database operation that  produces no result.
     *
     * @param op - operation impl.
     */
    void executeV(@NotNull DbOpV op);
}
