package com.github.mjdbc.test.asset.model;

import com.github.mjdbc.DbMapper;
import com.github.mjdbc.Mapper;

import java.sql.Timestamp;
import java.util.Objects;


/**
 * Sample class for user record in DB.
 * Fields are public because it's hard to find a reason to write both getters and setters.
 * Note: 'mjdbc' will work with getters too.
 */
public final class User {
    /**
     * It is recommended to have type safe IDs.
     */
    public UserId id;
    public String login;
    public String firstName;
    public String lastName;
    public Gender gender;
    public long score;
    public Timestamp registrationDate;

    /**
     * Class to create User object from result set.
     */
    @Mapper
    public static final DbMapper<User> MAPPER = (r) -> {
        User user = new User();
        user.id = new UserId(r.getInt("id"));
        user.login = r.getString("login");
        user.firstName = r.getString("first_name");
        user.lastName = r.getString("last_name");
        user.gender = Gender.fromDbValue(r.getInt("gender"));
        user.score = r.getLong("score");
        user.registrationDate = r.getTimestamp("reg_date");
        return user;
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return score == user.score &&
                Objects.equals(id, user.id) &&
                Objects.equals(login, user.login) &&
                Objects.equals(firstName, user.firstName) &&
                Objects.equals(lastName, user.lastName) &&
                gender == user.gender &&
                Objects.equals(registrationDate, user.registrationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, firstName, lastName, gender, score, registrationDate);
    }
}
