package mini.jdbc.test.asset.dbi;

import mini.jdbc.Tx;
import mini.jdbc.test.asset.model.User;
import mini.jdbc.test.asset.model.UserId;
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
    @Tx
    @Nullable
    User getUserById(@NotNull UserId id);

    /**
     * Gets user by login.
     *
     * @param login - user login to search. If null -> NullPointerException is thrown.
     * @return user or null if not found.
     */
    @Tx
    @Nullable
    User getUserByLogin(@NotNull String login);

    /**
     * Updates user's score, returns old value.
     */
    @Tx
    int updateScore(@NotNull String login, int newScore);


    /**
     * Updates user's score in database first. Throws RuntimeException at the end of the method to trigger rollback.
     */
    @Tx
    int updateScoreAndRollback(@NotNull String login, int newScore);

}
