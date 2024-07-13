package com.csci3130.group04.Daltweets.controller;

import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.csci3130.group04.Daltweets.model.User;
import com.csci3130.group04.Daltweets.model.User.Role;
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

    @PostMapping("/activate")
    ResponseEntity<Boolean> activateUser(@RequestBody Map<String, String> requestBody)
    {
      User admin = userService.getUserByName(requestBody.get("adminName"));
      if (!(admin.getRole().equals(Role.SUPERADMIN))) return new ResponseEntity<>(Boolean.FALSE,HttpStatus.UNAUTHORIZED);
      User activatedUser = userService.addExistingUser(requestBody.get("username"));
      if (activatedUser == null)
      {
        return new ResponseEntity<>(Boolean.FALSE,HttpStatus.NOT_IMPLEMENTED);
      }
      return  new ResponseEntity<>(Boolean.TRUE,HttpStatus.OK);
    }

    @PostMapping("/deactivate")
    ResponseEntity<Boolean> deactivateUser(@RequestBody Map<String, String> requestBody)
    {
      User admin = userService.getUserByName(requestBody.get("adminName"));
      if (!(admin.getRole().equals(Role.SUPERADMIN))) return new ResponseEntity<>(Boolean.FALSE,HttpStatus.UNAUTHORIZED);
      User deactivatedUser = userService.deleteExistingUser(requestBody.get("username"));
      if (deactivatedUser == null)
      {
        return new ResponseEntity<>(Boolean.FALSE,HttpStatus.NOT_IMPLEMENTED);
      }
      return  new ResponseEntity<>(Boolean.TRUE,HttpStatus.OK);
    }
}
