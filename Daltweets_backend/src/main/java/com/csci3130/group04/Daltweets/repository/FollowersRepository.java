package com.csci3130.group04.Daltweets.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.csci3130.group04.Daltweets.model.Followers;
import com.csci3130.group04.Daltweets.model.User;

@Repository
public interface FollowersRepository extends JpaRepository<Followers,Integer>{
   @Query("SELECT f.follower FROM Followers f WHERE f.user.id = :userId and status='ACCEPTED'")
    List<User> findFollowerIdsByUserId(@Param("userId") int userId);

    @Query("SELECT f.user FROM Followers f WHERE f.follower.id = :followerId")
    List<User> findUserIdsByFollowerId(@Param("followerId") int followerId);
    
    User findUserByFollowerId(User Follower);

    Followers findByUserAndFollower(User user, User follower);

    Followers findFollowersByUserIdAndFollowerId(User user, User follower);

    @Query("SELECT COALESCE(MAX(f.id), 0) FROM Followers f")
    int findLatestId();
} 
