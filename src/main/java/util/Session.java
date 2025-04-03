package util;

import model.User;

public class Session {
    private static User currentUser;

    public static void setUser(User user) {
        currentUser = user;
    }

    public static User getCurrentUser() {
        return currentUser;
    }
}
