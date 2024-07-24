package com.csci3130.group04.Daltweets.service;

import com.csci3130.group04.Daltweets.model.Post;
import com.csci3130.group04.Daltweets.model.PostLike;
import com.csci3130.group04.Daltweets.model.User;

public interface PostLikeService {
	int getPostLikes(Post post);
	PostLike addLike(User user, Post post);
	boolean postLikedByUser(User user, Post post);
	boolean unlike(User user, Post post);
}
