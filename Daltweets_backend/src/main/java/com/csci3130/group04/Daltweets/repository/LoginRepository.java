package com.csci3130.group04.Daltweets.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.csci3130.group04.Daltweets.model.Login;
import com.csci3130.group04.Daltweets.model.User;

public interface LoginRepository extends JpaRepository<Login,Integer> {
    Login findLoginByUserId(User user);
    
}