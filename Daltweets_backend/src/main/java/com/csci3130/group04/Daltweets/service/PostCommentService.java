package com.csci3130.group04.Daltweets.service;

import java.util.List;

import com.csci3130.group04.Daltweets.model.Post;
import com.csci3130.group04.Daltweets.model.PostComment;
import com.csci3130.group04.Daltweets.model.User;

public interface PostCommentService {
    PostComment createPostComment(Post post, User user, String comment);
    List<PostComment> getPostComments(Post post);
    public int getCommentCount(Post post);
} 
