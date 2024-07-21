package com.csci3130.group04.Daltweets.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.csci3130.group04.Daltweets.model.PostComment;

public interface PostCommentRepository extends JpaRepository<PostComment,Integer> {

    List<PostComment> findByPostId(int postId);
    
}
