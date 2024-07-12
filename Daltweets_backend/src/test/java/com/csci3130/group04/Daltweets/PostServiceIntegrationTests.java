package com.csci3130.group04.Daltweets;

import com.csci3130.group04.Daltweets.model.Post;
import com.csci3130.group04.Daltweets.model.User;
import com.csci3130.group04.Daltweets.repository.PostRepository;
import com.csci3130.group04.Daltweets.repository.UserRepository;
import com.csci3130.group04.Daltweets.service.Implementation.PostServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = DaltweetsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
class PostServiceIntegrationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PostServiceImpl postService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @AfterEach
    void teardown() {
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void createPostIntegrationTest() {
        User user = new User();
        user.setUsername("testUser");
        user = userRepository.save(user);

        Post post = new Post();
        post.setUser(user);
        post.setText("Test post");
        post.setDateCreated(LocalDateTime.now());

        ResponseEntity<Post> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/post/create", post, Post.class);

        assertNotNull(response.getBody());
        assertEquals("Test post", response.getBody().getText());
        assertEquals("testUser", response.getBody().getUser().getUsername());
    }

    @Test
    void getAllPostsIntegrationTest() {
        User user = new User();
        user.setUsername("testUser");
        user = userRepository.save(user);

        Post post1 = new Post();
        post1.setUser(user);
        post1.setText("Post 1");
        post1.setDateCreated(LocalDateTime.now());
        postRepository.save(post1);

        Post post2 = new Post();
        post2.setUser(user);
        post2.setText("Post 2");
        post2.setDateCreated(LocalDateTime.now());
        postRepository.save(post2);

        ResponseEntity<Post[]> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/post/testUser/all", Post[].class);

        List<Post> posts = List.of(response.getBody());

        assertEquals(2, posts.size());
        assertEquals("Post 1", posts.get(0).getText());
        assertEquals("Post 2", posts.get(1).getText());
    }
}
