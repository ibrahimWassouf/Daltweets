package com.csci3130.group04.Daltweets.repository;
import com.csci3130.group04.Daltweets.model.Post;
import com.csci3130.group04.Daltweets.model.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post,Integer> {

  List<Post> findPostsByUser(User user);

  List<Post> findPostsByUserIn(List<User> users);
    
}
