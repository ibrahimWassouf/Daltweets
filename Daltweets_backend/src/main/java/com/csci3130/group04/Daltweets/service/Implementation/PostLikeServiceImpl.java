package com.csci3130.group04.Daltweets.service.Implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csci3130.group04.Daltweets.model.Post;
import com.csci3130.group04.Daltweets.model.PostLike;
import com.csci3130.group04.Daltweets.model.User;
import com.csci3130.group04.Daltweets.repository.PostLikeRepository;
import com.csci3130.group04.Daltweets.service.PostLikeService;

@Service
public class PostLikeServiceImpl implements PostLikeService {

	@Autowired
	PostLikeRepository postLikeRepository;
	@Override
	public int getPostLikes(Post post) {
		if (post == null) {return 0;}
		return postLikeRepository.getLikeCount(post.getPostID());
	}
	
	@Override
	public PostLike addLike(User user, Post post) {
		if (post == null || user == null) return null;
		
		//prevents duplicate rows
		PostLike pl = postLikeRepository.postLikedByUser(user.getId(), post.getPostID());
		if (pl != null) return null;
		
		PostLike postLike= new PostLike(0, user, post);
		
		postLike = postLikeRepository.save(postLike);
		if (postLike == null) return null;
		return postLike;
		
	}

	@Override
	public boolean postLikedByUser(User user, Post post) {
		if (post == null || user == null) return false;
		PostLike pl = postLikeRepository.postLikedByUser(user.getId(), post.getPostID());
		return pl == null ? false : true;
	}

	@Override
	public boolean unlike(User user, Post post){
		if (post == null || user == null) return false;
		PostLike pl = postLikeRepository.findPostLikeByUserAndPost(user, post);
		 postLikeRepository.delete(pl);
		return true;
	}
	
}
