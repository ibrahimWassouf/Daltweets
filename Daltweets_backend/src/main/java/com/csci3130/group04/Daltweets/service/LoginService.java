package com.csci3130.group04.Daltweets.service;

import java.util.List;

import com.csci3130.group04.Daltweets.model.Login;

public interface LoginService {
    Boolean createLogin(Login login);
    public Login getLogin(String username); 
    public List<Login> getAll();
    public Login updatePassword(Login login);
}
