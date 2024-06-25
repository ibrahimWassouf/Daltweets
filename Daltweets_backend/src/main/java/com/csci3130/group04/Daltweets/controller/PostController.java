package com.csci3130.group04.Daltweets.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.csci3130.group04.Daltweets.model.Post;
import com.csci3130.group04.Daltweets.model.User;
import com.csci3130.group04.Daltweets.service.FollowersService;
import com.csci3130.group04.Daltweets.service.PostService;
import com.csci3130.group04.Daltweets.service.UserService;


@RestController
@RequestMapping("/api/post")
public class PostController {
    @Autowired
    PostService postService;

    @Autowired
    UserService userService;

    @Autowired
    FollowersService followersService;

    @GetMapping("/{username}/all")
    List<Post> getAllPosts(@PathVariable("username") String username)
    {
        User user = userService.getUserByName(username);
        List<User> followers = followersService.getUserFollowing(user);
        return postService.getPostsByUsers(followers);
    }
}
