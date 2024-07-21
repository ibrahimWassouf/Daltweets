package com.csci3130.group04.Daltweets.service;

import java.util.List;

import com.csci3130.group04.Daltweets.model.Post;
import com.csci3130.group04.Daltweets.model.User;

public interface PostService {
  Post createPost(Post post); 
  List<Post> getPostsByUser(User user);
  List<Post> getPostsByUsers(List<User> users);
  Post getPostById(int postId);
}
