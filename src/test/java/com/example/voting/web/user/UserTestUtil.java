package com.example.voting.web.user;

import com.example.voting.web.Matcher;
import com.example.voting.model.Role;
import com.example.voting.model.User;
import com.example.voting.util.JsonUtil;

public class UserTestUtil {
    public static final Matcher<User> MATCHER = new Matcher<>(User.class,"registered", "password");
    public static final int USER_ID = 1;
    public static final int ADMIN_ID = 2;
    public static final int USER_2_ID = 3;
    public static final int NOT_FOUND_ID = 20;
    public static final String ADMIN_MAIL = "admin@mail.com";
    public static final String USER_MAIL = "user@mail.com";
    public static final String USER_2_MAIL = "user2@mail.com";

    public static final User user = new User(USER_ID, "User", USER_MAIL, "password", Role.USER);
    public static final User user2 = new User(USER_2_ID, "User2", USER_2_MAIL, "password2", Role.USER);
    public static final User admin = new User(ADMIN_ID, "Admin", ADMIN_MAIL, "admin", Role.ADMIN, Role.USER);

    public static User getNew() {
        return new User(null, "User_new", "new@mail.com", "newpass", Role.USER);
    }

    public static User getUpdated() {
        return new User(USER_ID, "User_update", "update@mail.com", "password_update", Role.ADMIN);
    }

    public static String jsonWithPassword(User user, String passw) {
        return JsonUtil.writeAdditionProps(user, "password", passw);
    }
}
