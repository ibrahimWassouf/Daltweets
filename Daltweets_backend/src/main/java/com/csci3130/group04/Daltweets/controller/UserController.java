package com.csci3130.group04.Daltweets.controller;

import java.util.List;
import java.util.Map;

import com.csci3130.group04.Daltweets.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.csci3130.group04.Daltweets.model.User;
import com.csci3130.group04.Daltweets.model.Followers;
import com.csci3130.group04.Daltweets.model.Login;
import com.csci3130.group04.Daltweets.service.LoginService;
import com.csci3130.group04.Daltweets.service.Implementation.FollowersServiceImpl;
import com.csci3130.group04.Daltweets.service.Implementation.UserServiceImplementation;
import com.csci3130.group04.Daltweets.utils.SignUpRequestDTO;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserServiceImplementation userService;

    @Autowired
    FollowersServiceImpl followerService;

    @Autowired
    LoginService loginService;

    @PostMapping("/signup")
    User createUser(@RequestBody SignUpRequestDTO createUserRequestDto)
    {
        User user = createUserRequestDto.getUser();
        User createdUser = userService.createUser(user);

        Login login = createUserRequestDto.getLogin();

        login.setUsername(createdUser.getEmail());
        login.setUser(createdUser);

        loginService.createLogin(login);
        
       return createdUser;
    } 
    
    @PostMapping("/recommended-users")
    List<User> getRecommendedUsers(@RequestBody Map<String, String> requestBody){
      List<User> recommendList = userService.getRecommendedUsers(requestBody.get("username"));
      User user = userService.getUserByName(requestBody.get("username"));
      List<User> requests = followerService.getUserFollowing(user);
      
      for (int i = 0; i < recommendList.size(); i++){
        User recommendedUser = recommendList.get(i);
        for (int j = 0; j < requests.size(); j++){
          if (recommendedUser.getUsername().equals(requests.get(j).getUsername())){
            recommendList.remove(i);
          }
        }
      }

      return recommendList;
    }

    @GetMapping("/{username}/profile")
    User getUserProfile(@PathVariable String username){
        return userService.getUserByName(username);
    }

    @PutMapping("/update")
    User updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }
}
