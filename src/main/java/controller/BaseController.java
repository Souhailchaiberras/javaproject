package controller;

import model.User;

public class BaseController {
    private User user;

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public String getUsername() {
        return user != null ? user.getEmail() : "Guest";
    }
}
