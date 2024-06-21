package com.csci3130.group04.Daltweets.service.Implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csci3130.group04.Daltweets.repository.LoginRepository;
import com.csci3130.group04.Daltweets.service.LoginService;

@Service
public class LoginServiceImpl implements LoginService{
    @Autowired 
    LoginRepository loginRepository;

}
