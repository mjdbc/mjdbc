package mini.jdbc.test.asset.dbi;

import mini.jdbc.Db;
import mini.jdbc.DbStatement;
import mini.jdbc.Tx;
import mini.jdbc.test.asset.SampleQueries;
import mini.jdbc.test.asset.model.User;
import mini.jdbc.test.asset.model.UserId;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 */
public class SampleDbiImpl implements SampleDbi {

    private final Db db;
    private final SampleQueries queries;

    public SampleDbiImpl(Db db) {
        this.db = db;
        db.registerMapper(UserId.class, UserId.MAPPER);
        db.registerMapper(User.class, User.MAPPER);
        queries = db.attachQueries(SampleQueries.class);
    }

    @Tx
    @Nullable
    @Override
    public User getUserById(@NotNull UserId id) {
        return queries.getUserById(id);
    }

    @Tx
    @Nullable
    @Override
    public User getUserByLogin(final @NotNull String login) {
        return db.execute(c -> new DbStatement<>(c, "SELECT * FROM users WHERE login = :login", User.MAPPER).setString("login", login).query());
    }

    @Tx
    @Override
    public int updateScore(@NotNull String login, int newScore) {
        User user = queries.getUserByLogin(login);
        if (user == null) {
            return -1;
        }
        int oldScore = user.score;
        queries.updateScore(user.login, newScore);
        return oldScore;
    }

    @Tx
    @Override
    public int updateScoreAndRollback(@NotNull String login, int newScore) {
        User user = queries.getUserByLogin(login);
        if (user == null) {
            return -1;
        }
        queries.updateScore(user.login, newScore);
        throw new RuntimeException("Rollback!");
    }

    @Override
    @Tx
    public void createUser(@NotNull User user) {
        user.id = queries.insertUser(user);
    }
}
