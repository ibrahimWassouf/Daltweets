package com.csci3130.group04.Daltweets.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.csci3130.group04.Daltweets.model.Login;
import com.csci3130.group04.Daltweets.repository.LoginRepository;
import com.csci3130.group04.Daltweets.service.Implementation.LoginServiceImpl;

@RestController
@RequestMapping("/api/login")
public class LoginController {

    @Autowired
    LoginServiceImpl loginService;
    
    @Autowired
    LoginRepository loginRepository;

    @PostMapping("/")
    ResponseEntity<String> authenticate(@RequestBody Map<String, String> login){

      Login authentication = loginService.getLogin(login.get("username"));

      if (authentication == null || authentication.getUser() == null){
        return new ResponseEntity<String>("User not found", HttpStatus.NOT_FOUND);
      } else if (!authentication.getPassword().equals(login.get("password"))){
        return new ResponseEntity<String>("Incorrect Password", HttpStatus.BAD_REQUEST);
      }


      return new ResponseEntity<String>("User Authenticated", HttpStatus.OK);
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
