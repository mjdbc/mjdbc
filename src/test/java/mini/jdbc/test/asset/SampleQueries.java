package mini.jdbc.test.asset;


import mini.jdbc.Bind;
import mini.jdbc.Sql;
import mini.jdbc.test.asset.model.User;
import org.jetbrains.annotations.Nullable;

public interface SampleQueries {

    @Nullable
    @Sql("SELECT * FROM users WHERE login = :login")
    User selectUser(@Bind("login") String login);
}
