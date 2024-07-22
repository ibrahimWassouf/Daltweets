package com.csci3130.group04.Daltweets.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.csci3130.group04.Daltweets.model.PostComment;

public interface PostCommentRepository extends JpaRepository<PostComment,Integer> {

    List<PostComment> findByPostId(int postId);
    
    @Query("SELECT COUNT(pc) FROM PostComment pc WHERE pc.post.id = :postId")
int getCommentsCount(@Param("postId") int postId);

    
}
