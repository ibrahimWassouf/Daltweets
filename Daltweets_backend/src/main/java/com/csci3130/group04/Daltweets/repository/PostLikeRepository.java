package com.csci3130.group04.Daltweets.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.csci3130.group04.Daltweets.model.PostLike;

public interface PostLikeRepository extends JpaRepository<PostLike,Integer>{

	@Query("SELECT COUNT(pl) FROM PostLike pl WHERE pl.post.id = :postId")
	int getLikeCount(@Param("postId") int postId);
	
	@Query("SELECT pl FROM PostLike pl WHERE pl.post.id = :postId AND pl.user.id = :userId")
	boolean postLikedByUser(@Param("postId") int postId, @Param("userId") int userId);
}
