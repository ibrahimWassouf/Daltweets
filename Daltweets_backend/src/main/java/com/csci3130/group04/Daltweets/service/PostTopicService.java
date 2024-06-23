package com.csci3130.group04.Daltweets.service;

import com.csci3130.group04.Daltweets.model.*;
import org.springframework.stereotype.Repository;


import java.util.List;

public interface PostTopicService {
    public PostTopic createPostTopic(PostTopic postTopic);

    public List<Post> getPostByTopic(Topic topic);

    public List<Topic> getTopicByPost(Post post);

    public PostTopic deletePostTopic(Topic topic, Post post);
}
