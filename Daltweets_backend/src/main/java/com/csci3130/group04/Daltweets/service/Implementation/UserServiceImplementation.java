package com.csci3130.group04.Daltweets.service.Implementation;

import com.csci3130.group04.Daltweets.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csci3130.group04.Daltweets.repository.UserRepository;
import com.csci3130.group04.Daltweets.service.UserService;

import java.util.Optional;

@Service
public class UserServiceImplementation implements UserService {
    @Autowired
    UserRepository userRepository;
    
    @Override
    public User createUser(User user) {
        if ( user == null || user.getUsername() == null || user.getEmail() == null) {
            throw new IllegalArgumentException("user is empty");
        }
        userRepository.save(user);
        return user;
    }
    
    @Override
    public User getUserByName(String name) {
        if ( name == null ) {
            throw new IllegalArgumentException("name in get user is null");
        }
        return userRepository.findByUsername(name);
    }

    @Override
    public String updateUser(User updateUser) {
        if ( updateUser == null  ) {
            throw new IllegalArgumentException("user in update is NULL");
        }
        User user = getUserByName(updateUser.getUsername());
        if ( user == null ) {
            return "User not found to update";
        }
        user.setBio(updateUser.getBio());
        user.setEmail(updateUser.getEmail());
        user.setAccountDeleted(updateUser.isAccountDeleted());
        user.setStatus(updateUser.getStatus());
        userRepository.save(user);
        return "Update success";
    }
}
