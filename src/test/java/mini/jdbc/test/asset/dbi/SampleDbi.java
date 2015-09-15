package mini.jdbc.test.asset.dbi;

import mini.jdbc.Tx;
import mini.jdbc.test.asset.model.User;
import mini.jdbc.test.asset.model.UserId;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface SampleDbi {

    @Tx
    @Nullable
    User getUserById(@NotNull UserId id);

    @Tx
    @Nullable
    User getUserByLogin(@NotNull String login);

    /**
     * Updates user's score, returns old value.
     */
    @Tx
    int updateScore(@NotNull String login, int newScore);


    /**
     * Updates user's score and rolls back
     */
    @Tx
    int updateScoreAndRollback(@NotNull String login, int newScore);

    void createUser(@NotNull  User user);
}
