package mini.jdbc.test.asset.dbi;

import mini.jdbc.Db;
import mini.jdbc.DbStatement;
import mini.jdbc.Tx;
import mini.jdbc.test.asset.model.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 */
public class SampleDbiImpl implements SampleDbi {

    private final Db db;

    public SampleDbiImpl(Db db) {
        this.db = db;
    }

    @Tx
    @Nullable
    @Override
    public User getUserByLogin(final @NotNull String login) {
        return db.execute(c -> new DbStatement<>(c, "SELECT * FROM users WHERE login = :login", User.MAPPER).setString("login", login).query());
    }
}
