package com.csci3130.group04.Daltweets;

import com.csci3130.group04.Daltweets.model.Followers;
import com.csci3130.group04.Daltweets.model.Post;
import com.csci3130.group04.Daltweets.model.User;
import com.csci3130.group04.Daltweets.model.User.Role;

import com.csci3130.group04.Daltweets.repository.PostRepository;
import com.csci3130.group04.Daltweets.repository.UserRepository;
import com.csci3130.group04.Daltweets.repository.FollowersRepository;
import com.csci3130.group04.Daltweets.service.Implementation.FollowersServiceImpl;
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
        private PostRepository postRepository;

        @Autowired
        private FollowersServiceImpl followersService;

        @Autowired
        private FollowersRepository followersRepository;

        @Autowired
        private UserRepository userRepository;

        @BeforeEach
        void setup() {
                followersRepository.deleteAll();
                postRepository.deleteAll();
                userRepository.deleteAll();
        }

        @AfterEach
        void teardown() {
                followersRepository.deleteAll();
                postRepository.deleteAll();
                userRepository.deleteAll();
        }

        @Test
        void createPostIntegrationTest() {
                User user = new User(1, "PosterBio", "PostTester", "poster@dal.ca", LocalDateTime.now(), false,
                                Role.SUPERADMIN,
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
                User user = new User(1, "PosterBio", "PostTester", "poster@dal.ca", LocalDateTime.now(), false,
                                Role.SUPERADMIN,
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

        @Test
        void getAllPostsIntegrationTest() {
                // Create users
                User user = new User(1, "I follow people", "Following", "following@dal.ca", LocalDateTime.now(), false,
                                Role.SUPERADMIN, User.Status.ONLINE);
                User user2 = new User(2, "I will be followed", "Followed1", "followed1@dal.ca", LocalDateTime.now(),
                                false,
                                Role.SUPERADMIN, User.Status.ONLINE);
                User user3 = new User(3, "I'll also be followed", "Followed2", "followed2@dal.ca", LocalDateTime.now(),
                                false,
                                Role.SUPERADMIN, User.Status.ONLINE);

                user = userRepository.save(user);
                user2 = userRepository.save(user2);
                user3 = userRepository.save(user3);

                // Add followers
                Followers firstFollow = followersService.addFollower(user2, user);
                Followers secondFollow = followersService.addFollower(user3, user);

                followersRepository.save(firstFollow);
                followersRepository.save(secondFollow);

                // Create posts
                Post post2 = new Post();
                post2.setUser(user2);
                post2.setText("User 2's Post");
                post2.setDateCreated(LocalDateTime.now());

                Post post3 = new Post();
                post3.setUser(user3);
                post3.setText("User 3's Post");
                post3.setDateCreated(LocalDateTime.now());

                postRepository.save(post2);
                postRepository.save(post3);

                // Make the GET request to retrieve all posts for the specified user
                ResponseEntity<List> response = restTemplate.getForEntity(
                                "http://localhost:" + port + "/api/post/Following/all", List.class);

                // Verify the response
                assertNotNull(response.getBody());
                assertEquals(HttpStatus.OK, response.getStatusCode());
                assertEquals(2, response.getBody().size());
        }

}
