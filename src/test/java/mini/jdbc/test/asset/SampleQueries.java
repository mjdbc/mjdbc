package mini.jdbc.test.asset;


import mini.jdbc.Bind;
import mini.jdbc.BindBean;
import mini.jdbc.UseGeneratedKeys;
import mini.jdbc.Sql;
import mini.jdbc.test.asset.model.User;
import mini.jdbc.test.asset.model.UserId;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface SampleQueries {

    @Nullable
    @Sql("SELECT * FROM users WHERE id = :id")
    User getUserById(@Bind("id") UserId id);

    @Nullable
    @Sql("SELECT * FROM users WHERE login = :login")
    User getUserByLogin(@Bind("login") String login);

    @Sql("SELECT COUNT(*) FROM users")
    int countUsers();

    @NotNull
    @Sql("SELECT * FROM users")
    List<User> selectAllUsers();

    @Sql("UPDATE users SET score = :score  WHERE login = :login")
    void updateScore(@Bind("login") String login, @Bind("score") int score);

    @Sql("UPDATE users SET score = :score  WHERE login = :login")
    void updateScore(@BindBean User user);

    @NotNull
    @UseGeneratedKeys
    @Sql("INSERT INTO users(login, first_name, last_name, gender, score) VALUES (:login, :firstName, :lastName, :gender, :score)")
    UserId insertUser(@BindBean User user);
}
