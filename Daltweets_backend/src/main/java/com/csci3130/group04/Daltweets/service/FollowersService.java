package com.csci3130.group04.Daltweets.service;

import java.util.List;

import com.csci3130.group04.Daltweets.model.Followers;
import com.csci3130.group04.Daltweets.model.User;

public interface FollowersService {
    List<User> getAllFollowers(User user);
    List<User> getUserFollowing(User follower);
    Followers addFollower(User user, User follower);
    Boolean removeFollower(User user,User follower);  
    Boolean acceptFollowRequest(User user, User follower); 
    List<Followers> getFollowRequests(User user);
}
