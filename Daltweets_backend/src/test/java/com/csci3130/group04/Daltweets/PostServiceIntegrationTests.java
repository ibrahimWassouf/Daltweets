package com.csci3130.group04.Daltweets;

import com.csci3130.group04.Daltweets.model.Post;
import com.csci3130.group04.Daltweets.model.User;
import com.csci3130.group04.Daltweets.model.User.Role;

import com.csci3130.group04.Daltweets.repository.PostRepository;
import com.csci3130.group04.Daltweets.repository.UserRepository;

import com.csci3130.group04.Daltweets.service.Implementation.PostServiceImpl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

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

    @BeforeEach
    void setup() {
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    @AfterEach
    void teardown() {
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void createPostIntegrationTest() {
        User user = new User(1, "PosterBio", "PostTester", "poster@dal.ca", LocalDateTime.now(), false, Role.SUPERADMIN,
                User.Status.ONLINE);
        user = userRepository.save(user);

        Post post = new Post();
        post.setUser(user);
        post.setText("Post testers test post");
        post.setDateCreated(LocalDateTime.now());

        ResponseEntity<Post> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/post/create", post, Post.class);

        assertNotNull(response.getBody());
        assertEquals("Post testers test post", response.getBody().getText());
        assertEquals("PostTester", response.getBody().getUser().getUsername());
    }

    @Test
    void createPostWithNullUserIntegrationTest() {
        Post post = new Post();
        post.setText("Some text");
        post.setDateCreated(LocalDateTime.now());

        ResponseEntity<Post> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/post/create", post, Post.class);

        assertNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void createPostWithNullTextIntegrationTest() {
        User user = new User(1, "PosterBio", "PostTester", "poster@dal.ca", LocalDateTime.now(), false, Role.SUPERADMIN,
                User.Status.ONLINE);
        user = userRepository.save(user);

        Post post = new Post();
        post.setUser(user);
        post.setDateCreated(LocalDateTime.now());

        ResponseEntity<Post> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/post/create", post, Post.class);

        assertNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

}
