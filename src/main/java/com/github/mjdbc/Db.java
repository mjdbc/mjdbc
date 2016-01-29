package com.github.mjdbc;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * mJDBC database wrapper.
 * Allows to register result set mappers (@Mapper), statement parameter binders (@Binder), execute SQL queries and updates.
 */
public interface Db {

    /**
     * Registers result set mapper. See DbMapper class for details.
     *
     * @param mapperClass the result classes. Result set row will be mapped to the instance of this class.
     * @param mapper      mapper. This mapper will be used to map result set row to mapperClass instance.
     * @param <T>         result type.
     */
    <T> void registerMapper(@NotNull Class<T> mapperClass, @NotNull DbMapper<T> mapper);

    /**
     * Registers prepared statement parameter binder.
     * A binder for parameter is looked up by matching class first, any superclass second or any interface third.
     * The old binder for this type is removed.
     *
     * @param binderClass parameter class to be processed by this binder.
     * @param binder      binder implementation.
     */
    <T> void registerBinder(@NotNull Class<? extends T> binderClass, @NotNull DbBinder<T> binder);

    /**
     * Attaches Dbi (DB interfaces) implementation to the database.
     * All methods will be wrapped to transaction. Transaction will be started when the first SQL statement
     * is used inside of the method and closed on exit from the method. Transaction is committed
     * on normal exit and rolled back if Exception is thrown.
     * <p>
     * If Dbi method is called from another Dbi method no new transaction will be started: the upper-stack transaction will be used.
     *
     * @param impl         database interface implementation.
     * @param dbiInterface database interface to wrap with transactions support.
     * @param <T>          type of the Dbi interface.
     * @return wrapped instance of the @dbiInterface
     */
    @NotNull
    <T> T attachDbi(@NotNull T impl, @NotNull Class<T> dbiInterface);

    /**
     * Attaches sql interface to the database. All sql queries are parsed and validated during this call.
     *
     * @param sqlInterface sql interface to attach.
     * @param <T>          type of the interface.
     * @return interface implementation.
     */
    @NotNull
    <T> T attachSql(@NotNull Class<T> sqlInterface);

    /**
     * Executes database query that produces Nullable result.
     *
     * @param op  query impl.
     * @param <T> result type.
     * @return query result.
     */
    @Nullable
    <T> T execute(@NotNull DbOp<T> op);

    /**
     * Executes database query that produces NotNull result.
     *
     * @param op  query impl.
     * @param <T> result type.
     * @return query result.
     */
    @NotNull
    <T> T executeNN(@NotNull DbOpNN<T> op);

    /**
     * Executes database operation that  produces no result.
     *
     * @param op operation impl.
     */
    void executeV(@NotNull DbOpV op);


    /**
     * Returns per-method timers.
     * Statistics is collected for all methods of attached Dbi interfaces
     * and for all @Sql methods of attached query interfaces.
     *
     * @return per-method statistics. Note: This is direct access to the timers map. Timers are updated concurrently.
     * If a timer is removed from the map: it will be restarted.
     */
    @NotNull
    Map<Method, DbTimer> getTimers();


    /**
     * Factory method to obtain DB instance.
     *
     * @param ds {@link javax.sql.DataSource} to wrap.
     * @return new Db instance
     */
    @NotNull
    static Db newInstance(@NotNull DataSource ds) {
        return new DbImpl(ds);
    }

}
