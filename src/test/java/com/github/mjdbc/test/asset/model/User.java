package com.github.mjdbc.test.asset.model;

import com.github.mjdbc.DbMapper;

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
    public int score;

    /**
     * Class to create User object from result set.
     */
    public static DbMapper<User> MAPPER = (r) -> {
        User user = new User();
        user.id = new UserId(r.getInt("id"));
        user.login = r.getString("login");
        user.firstName = r.getString("first_name");
        user.lastName = r.getString("last_name");
        user.gender = Gender.fromDbValue(r.getInt("gender"));
        user.score = r.getInt("score");
        return user;
    };
}
