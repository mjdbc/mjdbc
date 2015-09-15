package mini.jdbc.test.asset.model;

import mini.jdbc.DbMapper;

public class User {
    public String login;
    public String firstName;
    public String lastName;
    public Gender gender;
    public int score;

    public static DbMapper<User> MAPPER = (r) -> {
        User user = new User();
        user.login = r.getString("login");
        user.firstName = r.getString("first_name");
        user.lastName = r.getString("last_name");
        user.gender = Gender.fromDbValue(r.getInt("gender"));
        user.score = r.getInt("score");
        return user;
    };
}
