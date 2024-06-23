package com.csci3130.group04.Daltweets.service.Implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csci3130.group04.Daltweets.model.Login;
import com.csci3130.group04.Daltweets.repository.LoginRepository;
import com.csci3130.group04.Daltweets.service.LoginService;

@Service
public class LoginServiceImpl implements LoginService{

    @Autowired 
    LoginRepository loginRepository;
  
    @Override
    public Boolean createLogin(Login login){
        if (login == null || login.getUser() == null ) return false;
        Login savedLogin = null; 
        if (login.getUsername().contains("@")|| login.getPassword().length() > 5) {
          savedLogin = loginRepository.save(login);
        }
        return savedLogin != null ? true : false;
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
