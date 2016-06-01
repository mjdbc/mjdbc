package com.github.mjdbc.test.asset.sql;


import com.github.mjdbc.Bind;
import com.github.mjdbc.BindBean;
import com.github.mjdbc.GetGeneratedKeys;
import com.github.mjdbc.Sql;
import com.github.mjdbc.test.asset.model.User;
import com.github.mjdbc.test.asset.model.UserId;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Set of raw SQL method to access to 'user' table data.
 */
public interface UserSql {

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

    @NotNull
    @Sql("SELECT id FROM users")
    List<UserId> selectAllUserIds();

    @NotNull
    @Sql("SELECT * FROM users WHERE score >= :minScore")
    List<User> selectAllUserByMinScore(@Bind("minScore") int score);

    @Sql("UPDATE users SET score = :score  WHERE login = :login")
    void updateScore(@Bind("login") String login, @Bind("score") int score);

    @Sql("UPDATE users SET score = :score  WHERE login = :login")
    void updateScore(@BindBean User user);

    @Sql("UPDATE users SET score = :score")
    int updateAllScores(@Bind("score") int newScore);

    @NotNull
    @GetGeneratedKeys
    @Sql("INSERT INTO users(login, first_name, last_name, gender, score, reg_date) VALUES (:login, :firstName, :lastName, :gender, :score, :registrationDate)")
    UserId insertUser(@BindBean User user);

    @Sql("DELETE FROM users")
    int deleteAll();
}
