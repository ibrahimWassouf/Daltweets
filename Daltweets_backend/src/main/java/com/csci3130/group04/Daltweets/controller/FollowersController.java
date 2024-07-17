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
import org.springframework.web.bind.annotation.RestController;

import com.csci3130.group04.Daltweets.model.Followers;
import com.csci3130.group04.Daltweets.model.User;
import com.csci3130.group04.Daltweets.service.Implementation.FollowersServiceImpl;
import com.csci3130.group04.Daltweets.service.Implementation.UserServiceImplementation;


@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/followers")
public class FollowersController {

    @Autowired
    FollowersServiceImpl followersService;

    @Autowired
    UserServiceImplementation userService;

    @PostMapping("/add")
    ResponseEntity<String> addFollower(@RequestBody Map<String, String> requestBody){
      User follower = userService.getUserByName(requestBody.get("follower"));
      User user = userService.getUserByName(requestBody.get("user"));

      Followers newFollowing = followersService.addFollower(user, follower);
      if (newFollowing == null){ return new ResponseEntity<>("Error sending follower request.", HttpStatus.BAD_REQUEST);}

      return new ResponseEntity<>("Follower request sent", HttpStatus.OK);
    }

    @PostMapping("/delete")
    ResponseEntity<String> deleteFollower(@RequestBody Map<String, String> requestBody){
      User follower = userService.getUserByName(requestBody.get("follower"));
      User user = userService.getUserByName(requestBody.get("user"));

      Boolean removeFollowing = followersService.removeFollower(user, follower);
      if (removeFollowing == false){ return new ResponseEntity<>("Error deleting follower request.", HttpStatus.BAD_REQUEST);}

      return new ResponseEntity<>("Follower request deleted", HttpStatus.OK);
    }
    
    @GetMapping("/{username}/requests")
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

    @GetMapping("/{username}/followers")
    ResponseEntity<List<User>> getFollowers(@PathVariable("username") String username)
    {
        User user = userService.getUserByName(username);
        return new ResponseEntity<List<User>>(followersService.getAllFollowers(user),HttpStatus.OK);
    }

    @GetMapping("/{username}/following")
    ResponseEntity<List<User>> getFollowing(@PathVariable("username") String username)
    {
        User user = userService.getUserByName(username);
        return new ResponseEntity<List<User>>(followersService.getUserFollowing(user),HttpStatus.OK);
    }

}
