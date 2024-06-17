package com.csci3130.group04.Daltweets.service.Implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csci3130.group04.Daltweets.repository.UserRepository;
import com.csci3130.group04.Daltweets.service.UserService;

@Service
public class UserServiceImplementation implements UserService {
    @Autowired
    UserRepository userRepository;
    
}
