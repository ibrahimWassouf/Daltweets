package com.csci3130.group04.Daltweets.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.csci3130.group04.Daltweets.model.Followers;
import com.csci3130.group04.Daltweets.model.User;
import com.csci3130.group04.Daltweets.service.FollowersService;
import com.csci3130.group04.Daltweets.service.UserService;

@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/follow")
public class FollowerController {

    @Autowired
    FollowersService followersService;

    @Autowired
    UserService userService;

    @GetMapping("/{userName}/requests")
    ResponseEntity<List<Followers>> getFollowRequests(@PathVariable("username") String username)
    {
        User user = userService.getUserByName(username);
        return new ResponseEntity<List<Followers>>(followersService.getFollowRequests(user),HttpStatus.OK);
    }
    
    @PostMapping("/accept")
    ResponseEntity<Boolean> acceptFollowRequest(@RequestBody Map<String,String> request)
    {
        User user = userService.getUserByName(request.get("username"));
        User follower = userService.getUserByName(request.get("followerName")); 
        return new ResponseEntity<Boolean>(followersService.acceptFollowRequest(user, follower), HttpStatus.OK);
    }

}
