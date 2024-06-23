package com.csci3130.group04.Daltweets.repository;

import java.util.List;

import com.csci3130.group04.Daltweets.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.csci3130.group04.Daltweets.model.PostTopic;
import com.csci3130.group04.Daltweets.model.Topic;

@Repository
public interface PostTopicRepository extends JpaRepository<PostTopic,Integer>{
    List<Topic> findByPostId(int postId);

    List<Post> findBytopic(Topic topic);

    List<PostTopic> findPostTopicBypost(Post post);

    List<PostTopic> findPostTopicBytopic(Topic topic);
}
