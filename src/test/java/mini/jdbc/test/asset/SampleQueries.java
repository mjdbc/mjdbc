package mini.jdbc.test.asset;


import mini.jdbc.Bind;
import mini.jdbc.BindBean;
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

    @Sql("UPDATE users SET score = :score  WHERE login = :login")
    void updateScore(@Bind("login") String login, @Bind("score") int score);

    @Sql("UPDATE users SET score = :score  WHERE login = :login")
    void updateScore(@BindBean User user);

}
