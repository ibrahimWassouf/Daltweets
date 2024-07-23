package com.csci3130.group04.Daltweets.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.csci3130.group04.Daltweets.model.Post;
import com.csci3130.group04.Daltweets.model.PostLike;
import com.csci3130.group04.Daltweets.model.User;

public interface PostLikeRepository extends JpaRepository<PostLike,Integer>{

	@Query("SELECT COUNT(pl) FROM PostLike pl WHERE pl.post.id = :postId")
	int getLikeCount(@Param("postId") int postId);
	
	@Query("SELECT pl FROM PostLike pl WHERE pl.post.id = :postId AND pl.user.id = :userId")
	PostLike postLikedByUser( @Param("userId") int userId, @Param("postId") int postId);

	PostLike findPostLikeByUserAndPost(User user, Post post);
}
