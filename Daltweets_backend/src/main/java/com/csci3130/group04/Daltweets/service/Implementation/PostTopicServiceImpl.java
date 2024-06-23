package com.csci3130.group04.Daltweets.service.Implementation;

import com.csci3130.group04.Daltweets.model.Post;
import com.csci3130.group04.Daltweets.model.PostTopic;
import com.csci3130.group04.Daltweets.model.Topic;
import com.csci3130.group04.Daltweets.repository.PostRepository;
import com.csci3130.group04.Daltweets.repository.PostTopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostTopicServiceImpl {

    @Autowired
    PostTopicRepository postTopicRepository;
    public PostTopic createPostTopic(PostTopic postTopic) {
        return null;
    }

    public List<Post> getPostByTopic(Topic topic) {
        return null;
    }

    public List<Topic> getTopicByPost(Post post) {
        return null;
    }

    public PostTopic deletePostTopic(Topic topic, Post post) {
        return null;
    }
}
