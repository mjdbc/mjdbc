package com.github.mjdbc.test.asset.dbi;

import com.github.mjdbc.test.asset.model.User;
import com.github.mjdbc.test.asset.model.UserId;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Database interface used in demo tests.
 */
public interface SampleDbi {

    /**
     * Creates new user. Assigns id. Throws NullPointerException if user is null.
     *
     * @param user - user to create;
     */
    void createUser(@NotNull User user);

    /**
     * Gets user by id.
     *
     * @param id - user id to search. If null -> NullPointerException is thrown.
     * @return user or null if not found.
     */
    @Nullable
    User getUserById(@NotNull UserId id);

    /**
     * Gets user by login.
     *
     * @param login - user login to search. If null -> NullPointerException is thrown.
     * @return user or null if not found.
     */
    @Nullable
    User getUserByLogin(@NotNull String login);

    /**
     * Updates user's score, returns old value.
     */
    long updateScore(@NotNull String login, int newScore);

    /**
     * Updates user's score in database first. Throws RuntimeException at the end of the method to trigger rollback.
     */
    int updateScoreAndRollback(@NotNull String login, int newScore);

    /**
     * Special method to demonstrate that if cache is used no connection is queried from DataSource at all
     */
    int getValueUseCacheIfPossible(boolean useCache);

}
