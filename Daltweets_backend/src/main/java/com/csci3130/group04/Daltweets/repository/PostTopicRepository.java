package com.csci3130.group04.Daltweets.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.csci3130.group04.Daltweets.model.PostTopic;
import com.csci3130.group04.Daltweets.model.Topic;

@Repository
public interface PostTopicRepository extends JpaRepository<PostTopic,Integer>{
    List<Topic> findByPostId(int postId);
}
