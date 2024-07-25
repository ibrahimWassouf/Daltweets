package com.csci3130.group04.Daltweets.service.Implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csci3130.group04.Daltweets.model.Login;
import com.csci3130.group04.Daltweets.repository.LoginRepository;
import com.csci3130.group04.Daltweets.service.LoginService;

@Service
public class LoginServiceImpl implements LoginService{
    private final int PASSWORD_LENGTH = 8;

    @Autowired 
    LoginRepository loginRepository;
  
    @Override
    public Boolean createLogin(Login login){
        if (login == null || login.getUser() == null ) return false;
        Login savedLogin = null; 
        if (login.getUsername().contains("@dal.ca") && validatePassword(login.getPassword())) {
          savedLogin = loginRepository.save(login);
        }
        return savedLogin != null ? true : false;
    }

    private boolean validatePassword(String password)
    {
      if(password == null || password.length() < PASSWORD_LENGTH)
      {
          return false;
      }

      boolean hasUppercase = false;
      boolean hasLowercase = false;
      boolean hasDigit = false;
      boolean hasSpecialChar = false;
      String specialChars = "!@#$%^&*()-+=";

      for (char ch : password.toCharArray()) {
          if (Character.isUpperCase(ch)) {
              hasUppercase = true;
          } else if (Character.isLowerCase(ch)) {
              hasLowercase = true;
          } else if (Character.isDigit(ch)) {
              hasDigit = true;
          } else if (specialChars.contains(String.valueOf(ch))) {
              hasSpecialChar = true;
          }
      }

      return hasUppercase && hasLowercase && hasDigit && hasSpecialChar;
    }

    @Override
    public Login getLogin(String username){
      return loginRepository.findByUsername(username); 
    }

    @Override
    public List<Login> getAll(){
      return loginRepository.findAll();
    }

    @Override
    public Login updatePassword(Login login){
      return loginRepository.save(login);
    }
}
