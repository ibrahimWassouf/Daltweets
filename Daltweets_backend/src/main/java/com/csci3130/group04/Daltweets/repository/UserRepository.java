package com.csci3130.group04.Daltweets.repository;
import  com.csci3130.group04.Daltweets.model.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    @Query("SELECT u FROM User u WHERE u.username = :name and status != 'DEACTIVATED'")
    User findByUsername(@Param("name") String name);

    @Query("SELECT u FROM User u WHERE u.username != :name and  status != 'DEACTIVATED'")
    List<User> findByUsernameNot(String name);

    @Query("SELECT u FROM User u WHERE u.username = :name") 
    User findByUsernameRawSearch(@Param("name") String name);
}
