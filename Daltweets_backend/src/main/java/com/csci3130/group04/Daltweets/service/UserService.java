package com.csci3130.group04.Daltweets.service;

import com.csci3130.group04.Daltweets.model.User;

public interface UserService {
    public User createUser(User user);

    public User getUserByName(String name);

    public String updateUser(User user);

}
