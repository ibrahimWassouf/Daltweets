package com.csci3130.group04.Daltweets.utils;

import com.csci3130.group04.Daltweets.model.Login;
import com.csci3130.group04.Daltweets.model.User;

public class SignUpRequestDTO {
    private User user;
    private Login login;

    public SignUpRequestDTO() {
    }

    public SignUpRequestDTO(User user, Login login) {
        this.user = user;
        this.login = login;
    }

    public Login getLogin() {
        return login;
    }
    public void setLogin(Login login) {
        this.login = login;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
}