package com.csci3130.group04.Daltweets.service.Implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csci3130.group04.Daltweets.model.Post;
import com.csci3130.group04.Daltweets.model.User;
import com.csci3130.group04.Daltweets.repository.PostRepository;
import com.csci3130.group04.Daltweets.service.PostService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {
    @Autowired
    PostRepository postRepository;

    public Post createPost(Post post){
      if (post.getUser() == null || post.getText() == null) return null;
      if (post.getDateCreated() == null){
        post.setDateCreated(LocalDateTime.now());
      }

      return postRepository.save(post);
    }

    public List<Post> getPostsByUser(User user){
      if (user == null) return null;

      return postRepository.findPostsByUser(user);
    }
    
    public List<Post> getPostsByUsers(List<User> users){
      for (int i = 0; i < users.size(); i++){
        if (users.get(i) == null){ users.remove(i);}
      } 

      return postRepository.findPostsByUserIn(users);
    }

    @Override
    public Post getPostById(int postId) {
      Optional<Post> optionalPost = postRepository.findById(postId);
      if (optionalPost == null || !optionalPost.isPresent()) return null;
      return optionalPost.get();
    }

}
