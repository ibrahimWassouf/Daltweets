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

    public Login getLogin(String username){
      return loginRepository.findByUsername(username); 
    }

    public List<Login> getAll(){
      return loginRepository.findAll();
    }

    public Login updatePassword(Login login){
      return loginRepository.save(login);
    }
}
