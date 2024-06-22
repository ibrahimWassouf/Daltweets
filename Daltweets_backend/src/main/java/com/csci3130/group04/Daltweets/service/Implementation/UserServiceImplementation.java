package com.csci3130.group04.Daltweets.service.Implementation;

import com.csci3130.group04.Daltweets.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csci3130.group04.Daltweets.repository.UserRepository;
import com.csci3130.group04.Daltweets.service.UserService;

@Service
public class UserServiceImplementation implements UserService {
    @Autowired
    UserRepository userRepository;
    
    @Override
    public User createUser(User user) {
        return null;
    }
    
    @Override
    public User getUserByName(String name) {
       return null;
    }

    @Override
    public String updateUser(User user) {
        return null;
    }
}
