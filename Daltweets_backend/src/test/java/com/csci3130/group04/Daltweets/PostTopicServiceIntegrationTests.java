package com.csci3130.group04.Daltweets;

import com.csci3130.group04.Daltweets.model.Post;
import com.csci3130.group04.Daltweets.model.PostTopic;
import com.csci3130.group04.Daltweets.model.Topic;
import com.csci3130.group04.Daltweets.repository.PostRepository;
import com.csci3130.group04.Daltweets.repository.PostTopicRepository;
import com.csci3130.group04.Daltweets.repository.TopicRepository;
import com.csci3130.group04.Daltweets.service.Implementation.PostTopicServiceImpl;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@Transactional
public class PostTopicServiceIntegrationTests {
    @Autowired
    private PostTopicServiceImpl postTopicService;

    @Autowired
    private PostTopicRepository postTopicRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private TopicRepository topicRepository;

    @Test
    public void testCreatePostTopic() {
        PostTopic postTopic = new PostTopic();
        Post post = new Post();
        post.setText("Test post");


        Topic topic = new Topic();
        topic.setName("Test topic");
        postTopic.setPost(post);
        postTopic.setTopic(topic);

        postRepository.save(post);
        topicRepository.save(topic);

        PostTopic createdPostTopic = postTopicService.createPostTopic(postTopic);
        assertEquals(createdPostTopic.getPost(),postTopic.getPost());
        assertEquals(createdPostTopic.getTopic(),postTopic.getTopic());
    }

    @Test
    public void testCreatePostTopicWithNULL() {
        assertThrows(Throwable.class,() -> postTopicService.createPostTopic(null));
    }

    @Test
    public void testGetPostByTopic() {
        PostTopic postTopic = new PostTopic();
        Post post = new Post();
        post.setText("Test post");
        Topic topic = new Topic();
        topic.setName("Test topic");
        postTopic.setPost(post);
        postTopic.setTopic(topic);

        postRepository.save(post);
        topicRepository.save(topic);

        PostTopic createdPostTopic = postTopicService.createPostTopic(postTopic);
        List<Post> posts = postTopicService.getPostByTopic(topic);
        assertEquals(post.getText(),posts.get(0).getText());
    }

    @Test
    public void testGetPostByTopicWithNull() {
        assertThrows(Throwable.class,() -> postTopicService.getPostByTopic(null));
    }

    @Test
    public void testGetPostByTopicWithNoExistPost() {
        PostTopic postTopic = new PostTopic();
        Post post = new Post();
        post.setText("Test post");
        Topic topic = new Topic();
        topic.setName("Test topic");
        postTopic.setPost(post);
        postTopic.setTopic(topic);

        postRepository.save(post);
        topicRepository.save(topic);

        List<Post> posts = postTopicService.getPostByTopic(topic);
        assertEquals(0,posts.size());
    }
    @Test
    public void testGetTopicByPost() {
        PostTopic postTopic = new PostTopic();
        Post post = new Post();
        post.setText("Test post");
        Topic topic = new Topic();
        topic.setName("Test topic");
        postTopic.setPost(post);
        postTopic.setTopic(topic);

        postRepository.save(post);
        topicRepository.save(topic);

        PostTopic createdPostTopic = postTopicService.createPostTopic(postTopic);
        List<Topic> topics = postTopicService.getTopicByPost(post);
        assertEquals(topic.getName(),topics.get(0).getName());
    }

    @Test
    public void testGetTopicByPostWithNull() {
        assertThrows(Throwable.class,() -> postTopicService.getTopicByPost(null));
    }

    @Test
    public void testGetTopicByPostWithNoExistPost() {
        PostTopic postTopic = new PostTopic();
        Post post = new Post();
        post.setText("Test post");
        Topic topic = new Topic();
        topic.setName("Test topic");
        postTopic.setPost(post);
        postTopic.setTopic(topic);

        postRepository.save(post);
        topicRepository.save(topic);

        List<Topic> topics = postTopicService.getTopicByPost(post);
        assertEquals(0,topics.size());
    }

    @Test
    public void testDeleteTopic() {
        PostTopic postTopic = new PostTopic();
        Post post = new Post();
        post.setText("Test post");
        Topic topic = new Topic();
        topic.setName("Test topic");
        postTopic.setPost(post);
        postTopic.setTopic(topic);

        postRepository.save(post);
        topicRepository.save(topic);

        PostTopic createdPostTopic = postTopicService.createPostTopic(postTopic);

        PostTopic deletedTopic = postTopicService.deletePostTopic(topic,post);

        assertEquals(deletedTopic.getTopic(),topic);
        assertEquals(deletedTopic.getPost(),post);
    }

    @Test
    public void testDeleteTopicWithNoExistTopic() {
        PostTopic postTopic = new PostTopic();
        Post post = new Post();
        post.setText("Test post");
        Topic topic = new Topic();
        topic.setName("Test topic");
        postTopic.setPost(post);
        postTopic.setTopic(topic);

        postRepository.save(post);
        topicRepository.save(topic);

        PostTopic deletedTopic = postTopicService.deletePostTopic(topic,post);

        assertNull(deletedTopic);
    }

    @Test
    public void testDeleteTopicWithNoExistPost() {
        PostTopic postTopic = new PostTopic();
        Post post = new Post();
        post.setText("Test post");
        Topic topic = new Topic();
        topic.setName("Test topic");
        postTopic.setPost(post);
        postTopic.setTopic(topic);
        Post new_post = new Post();
        new_post.setText("New post");

        postRepository.save(post);
        topicRepository.save(topic);
        postRepository.save(new_post);

        PostTopic createdPostTopic = postTopicService.createPostTopic(postTopic);
        PostTopic deletedTopic = postTopicService.deletePostTopic(topic,new_post);

        assertNull(deletedTopic);
    }

    @Test
    public void testDeleteTopicWithNull() {
        PostTopic postTopic = new PostTopic();
        Post post = new Post();
        post.setText("Test post");
        Topic topic = new Topic();
        topic.setName("Test topic");
        postTopic.setPost(post);
        postTopic.setTopic(topic);

        postRepository.save(post);
        topicRepository.save(topic);

        assertThrows(Throwable.class,() -> postTopicService.deletePostTopic(null,post));
        assertThrows(Throwable.class,() -> postTopicService.deletePostTopic(topic,null));
    }
}
