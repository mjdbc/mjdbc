package com.github.mjdbc.test.asset.dbi;

import com.github.mjdbc.DbStatement;
import com.github.mjdbc.Tx;
import com.github.mjdbc.test.asset.model.User;
import com.github.mjdbc.Db;
import com.github.mjdbc.test.asset.UserSql;
import com.github.mjdbc.test.asset.model.UserId;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * SampleDbi implementation. All methods annotated with @Tx will be wrapped with transaction context.
 */
public class SampleDbiImpl implements SampleDbi {

    /**
     * Internal link to Database. May be useful to perform some low-level ops.
     */
    private final Db db;

    /**
     * Set of raw SQL queries. All these queries are parsed and checked on startup (when interface is attached).
     */
    private final UserSql userSql;

    /**
     * Public constructor. Follow inlined comments.
     */
    public SampleDbiImpl(Db db) {
        // database reference can be useful for low-level queries.
        this.db = db;

        // register some mappers to be used automatically in all queries.
        db.registerMapper(UserId.class, UserId.MAPPER);
        db.registerMapper(User.class, User.MAPPER);

        // attach (parse, check, get proxy implementation) all queries in SampleQueries interface.
        userSql = db.attachSql(UserSql.class);
    }

    /**
     * {@inheritDoc}
     */
    @Tx
    @Override
    public void createUser(@NotNull User user) {
        Objects.requireNonNull(user);
        // insert user using sql interface and assign id.
        user.id = userSql.insertUser(user);
    }

    /**
     * {@inheritDoc}
     */
    @Tx
    @Nullable
    @Override
    public User getUserByLogin(@NotNull String login) {
        Objects.requireNonNull(login);
        return db.execute(c -> new DbStatement<>(c, "SELECT * FROM users WHERE login = :login", User.MAPPER).setString("login", login).query());
    }

    /**
     * {@inheritDoc}
     */
    @Tx
    public User getUserById(@NotNull UserId id) {
        Objects.requireNonNull(id);
        return userSql.getUserById(id);
    }


    /**
     * {@inheritDoc}
     */
    @Tx
    @Override
    public int updateScore(@NotNull String login, int newScore) {
        // use sql interface to perform sql query.
        // mapper registered in the constructor will be used to map result class.
        User user = userSql.getUserByLogin(login);
        if (user == null) {
            return -1;
        }

        int oldScore = user.score;
        // use sql interface again. Note: this call is in the same transaction!
        userSql.updateScore(user.login, newScore);
        return oldScore;
    }


    /**
     * {@inheritDoc}
     */
    @Tx
    @Override
    public int updateScoreAndRollback(@NotNull String login, int newScore) {
        User user = userSql.getUserByLogin(login);
        if (user == null) {
            return -1;
        }
        userSql.updateScore(user.login, newScore);

        // trigger rollback and undo score update.
        throw new RuntimeException("Rollback!");
    }
}
