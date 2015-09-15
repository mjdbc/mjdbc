package mini.jdbc.test.asset;


import mini.jdbc.Bind;
import mini.jdbc.Sql;
import mini.jdbc.test.asset.model.User;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface SampleQueries {

    @Nullable
    @Sql("SELECT * FROM users WHERE login = :login")
    User selectUser(@Bind("login") String login);

    @Sql("SELECT COUNT(*) FROM users")
    int countUsers();

    @Sql("SELECT * FROM users")
    List<User> selectAllUsers();

}
