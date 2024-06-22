package com.csci3130.group04.Daltweets.repository;
import  com.csci3130.group04.Daltweets.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    User findByUsername(String name);
}
