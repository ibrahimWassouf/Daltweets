package com.csci3130.group04.Daltweets.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.csci3130.group04.Daltweets.model.User;
import com.csci3130.group04.Daltweets.model.Login;
import com.csci3130.group04.Daltweets.service.LoginService;
import com.csci3130.group04.Daltweets.service.Implementation.UserServiceImplementation;
import com.csci3130.group04.Daltweets.utils.SignUpRequestDTO;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserServiceImplementation userService;

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
    
    @GetMapping("/recommended-users")
    List<User> getRecommendedUsers(@RequestBody Map<String, String> requestBody){
      return userService.getRecommendedUsers(requestBody.get("username"));
    }

    @GetMapping("/profile/{username}")
    User getUserProfile(@RequestBody Map<String, String> requestBody, @PathVariable String username){
        return userService.getUserByName(requestBody.get(username)); //might change
    }

}
