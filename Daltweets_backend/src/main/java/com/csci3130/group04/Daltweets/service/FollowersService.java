package com.csci3130.group04.Daltweets.service;

import java.util.List;

import com.csci3130.group04.Daltweets.model.Followers;
import com.csci3130.group04.Daltweets.model.User;

public interface FollowersService {
    List<User> getAllFollowers(User user);
    Followers addFollower(User user, User follower);
    Boolean removeFollower(User user,User follower);   
}
