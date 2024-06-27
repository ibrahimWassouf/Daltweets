package com.csci3130.group04.Daltweets.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.csci3130.group04.Daltweets.model.Login;
import com.csci3130.group04.Daltweets.model.User;
import com.csci3130.group04.Daltweets.service.Implementation.LoginServiceImpl;
import com.csci3130.group04.Daltweets.service.Implementation.UserServiceImplementation;

@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/login")
public class LoginController {

    @Autowired
    LoginServiceImpl loginService;

    @Autowired
    UserServiceImplementation userService;

    @PostMapping("/")
    ResponseEntity<User> authenticate(@RequestBody Map<String, String> login){

      Login authentication = loginService.getLogin(login.get("username"));

      if (authentication == null || authentication.getUser() == null){
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
      } else if (!authentication.getPassword().equals(login.get("password"))){
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
      }

      User user = authentication.getUser();
      return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/get-security-question")
    ResponseEntity<String> getSecurityQ(@RequestBody Map<String, String> requestBody){
      Login loginEntry = loginService.getLogin(requestBody.get("username"));

      if (loginEntry == null || loginEntry.getUser() == null){
        return new ResponseEntity<String>("User not found", HttpStatus.NOT_FOUND);
      }

      return new ResponseEntity<String>(loginEntry.getSecurityQuestion(), HttpStatus.OK);
    }

    @PostMapping("/change-password")
    ResponseEntity<String> changePassword(@RequestBody Map<String, String> requestBody){
      Login loginEntry = loginService.getLogin(requestBody.get("username"));

      if (loginEntry == null || loginEntry.getUser() == null){
        return new ResponseEntity<String>("User not found", HttpStatus.NOT_FOUND);
      } else if (!loginEntry.getPassword().equals(requestBody.get("password"))){
        return new ResponseEntity<String>("Incorrect Password", HttpStatus.BAD_REQUEST);
      }
      
      loginEntry.setPassword(requestBody.get("new-password"));
      loginService.updatePassword(loginEntry);

      return new ResponseEntity<>("Password updated", HttpStatus.OK);

    }
}
