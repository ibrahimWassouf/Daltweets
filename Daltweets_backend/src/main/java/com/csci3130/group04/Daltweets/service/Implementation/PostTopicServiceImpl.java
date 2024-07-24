package com.csci3130.group04.Daltweets.service.Implementation;

import com.csci3130.group04.Daltweets.model.Post;
import com.csci3130.group04.Daltweets.model.PostTopic;
import com.csci3130.group04.Daltweets.model.Topic;
import com.csci3130.group04.Daltweets.repository.PostRepository;
import com.csci3130.group04.Daltweets.repository.PostTopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ObjectStreamException;
import java.util.ArrayList;
import java.util.List;

@Service
public class PostTopicServiceImpl {

    @Autowired
    PostTopicRepository postTopicRepository;
    public PostTopic createPostTopic(PostTopic postTopic) {
        if ( postTopic == null ) throw new IllegalArgumentException("Null in createPostTopic");

        PostTopic found_postTopic = postTopicRepository.findPostTopicByPostAndTopic(postTopic.getPost(),postTopic.getTopic());
        if ( found_postTopic != null ) return found_postTopic;

        postTopicRepository.save(postTopic);
        return postTopic;
    }

    public List<Post> getPostByTopic(Topic topic) {
        if ( topic == null ) {
            throw new IllegalArgumentException("Null in getPostByTopic");
        }
        List<PostTopic> postTopics = postTopicRepository.findPostTopicBytopic(topic);
        List<Post> posts = new ArrayList<>();
        for ( int i = 0 ; i < postTopics.size() ; ++i ) {
            Post post = postTopics.get(i).getPost();
            posts.add(post);
        }
        return posts;

    }

    public List<Topic> getTopicByPost(Post post) {
        if ( post == null ) {
            throw new IllegalArgumentException("Null in getTopicByPost");
        }
        List<PostTopic> postTopics = postTopicRepository.findPostTopicBypost(post);
        List<Topic> topics = new ArrayList<>();
        for ( int i = 0 ; i < postTopics.size() ; ++i ) {
            Topic topic = postTopics.get(i).getTopic();
            topics.add(topic);
        }
        return topics;
    }

    public PostTopic deletePostTopic(Topic topic, Post post) {
        if ( topic == null ) throw new IllegalArgumentException("topic is Null in deletePostTopic");
        if ( post == null ) throw new IllegalArgumentException("post is Null in deletePostTopic");

        List<PostTopic> postTopics = postTopicRepository.findPostTopicBytopic(topic);

        if ( postTopics.isEmpty() ) return null;
        for ( int i = 0 ; i < postTopics.size() ; ++i ) {
            PostTopic postTopic = postTopics.get(i);
            Post saved_post = postTopic.getPost();
            if ( saved_post.equals(post) ) {
                postTopicRepository.delete(postTopic);
                return postTopic;
            }
        }
        return null;
    }
}
