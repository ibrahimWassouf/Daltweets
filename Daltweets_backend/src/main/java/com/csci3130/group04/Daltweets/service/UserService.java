package com.csci3130.group04.Daltweets.service;

import java.util.List;

import com.csci3130.group04.Daltweets.model.User;

public interface UserService {
    public User createUser(User user);

    public User getUserByName(String name);

    public User updateUser(User user);

    public List<User> getRecommendedUsers(String name);

    public User addExistingUser(String name);

    public User softDeleteUser(String name);
    public User changeUserStatus(String username, User.Status status );
    public boolean isValidName(String name);

    public List<User> getSignupRequests();

    public List<User> getAllUsers();
}
