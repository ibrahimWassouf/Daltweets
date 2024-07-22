package com.csci3130.group04.Daltweets.service.Implementation;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csci3130.group04.Daltweets.model.Post;
import com.csci3130.group04.Daltweets.model.PostComment;
import com.csci3130.group04.Daltweets.model.User;
import com.csci3130.group04.Daltweets.repository.PostCommentRepository;
import com.csci3130.group04.Daltweets.service.PostCommentService;

@Service
public class PostCommentServiceImpl implements PostCommentService {

    @Autowired
    PostCommentRepository postCommentRepository;

    @Override
    public PostComment createPostComment(Post post, User user, String comment) {
        if (post == null || user == null || comment == null) return null;
        LocalDateTime dateCreated = LocalDateTime.now();
        PostComment postComment = new PostComment(0,user,post, comment, dateCreated);
        return postCommentRepository.save(postComment);
    }

    @Override
    public List<PostComment> getPostComments(Post post) {
       if (post == null) return null;
       return postCommentRepository.findByPostId(post.getPostID());
    }

    @Override
    public int getCommentCount(Post post) {
        if(post == null) return 0;
        return postCommentRepository.getCommentsCount(post.getPostID());
    }
    
}
