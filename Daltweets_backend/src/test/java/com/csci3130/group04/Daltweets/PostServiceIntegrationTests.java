package com.csci3130.group04.Daltweets;

import com.csci3130.group04.Daltweets.model.*;
import com.csci3130.group04.Daltweets.model.User.Role;

import com.csci3130.group04.Daltweets.repository.*;
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
public class PostServiceIntegrationTests {

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
        @Autowired
        private GroupRepository groupRepository;
        @Autowired
        private GroupMembersRepository groupMembersRepository;

        @AfterEach
        void teardown() {

                followersRepository.deleteAll();
                postRepository.deleteAll();
                groupMembersRepository.deleteAll();
                groupRepository.deleteAll();
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
                user = userRepository.save(user);
                User user2 = new User(user.getId() + 1,  "I will be followed", "Followed1", "followed1@dal.ca", LocalDateTime.now(),
                                false,
                                Role.SUPERADMIN, User.Status.ONLINE);
                user2 = userRepository.save(user2);
                User user3 = new User(user2.getId()+1, "I'll also be followed", "Followed2", "followed2@dal.ca", LocalDateTime.now(),
                                false,
                                Role.SUPERADMIN, User.Status.ONLINE);
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
        @Test
        public void test_get_all_group_posts() {
                Group group = new Group(1, "group1", LocalDateTime.now(), false,"");
                Group saved_group = groupRepository.save(group);

                User user = new User(1, "checkbio2", "Name2", "mail2", LocalDateTime.now(), false, User.Role.SUPERADMIN, User.Status.ONLINE);
                User saved_user = userRepository.save(user);

                GroupMembers groupMembers = new GroupMembers(1,saved_group,saved_user,true);
                GroupMembers saved_groupMembers = groupMembersRepository.save(groupMembers);

                Post post1 = new Post(1, saved_user, "my first post", LocalDateTime.now(), false, false);
                Post sentPost1 = postService.createPost(post1);
                int postNum = 1;

                ResponseEntity<List> response = this.restTemplate.getForEntity("http://localhost:" + port + "/api/post/" + saved_group.getName() + "/groupPosts", List.class);

                assertNotNull(response);
                assertEquals(postNum,response.getBody().size());
                assertEquals(HttpStatus.OK,response.getStatusCode());
        }

        @Test
        public void test_get_all_group_posts_with_null() {
                Group group = new Group(1, null, LocalDateTime.now(), false,"");
                Group saved_group = groupRepository.save(group);

                User user = new User(1, "checkbio2", "Name2", "mail2", LocalDateTime.now(), false, User.Role.SUPERADMIN, User.Status.ONLINE);
                User saved_user = userRepository.save(user);

                GroupMembers groupMembers = new GroupMembers(1,saved_group,saved_user,true);
                GroupMembers saved_groupMembers = groupMembersRepository.save(groupMembers);

                Post post1 = new Post(1, saved_user, "my first post", LocalDateTime.now(), false, false);
                Post sentPost1 = postService.createPost(post1);
                int postNum = 1;

                ResponseEntity<List> response = this.restTemplate.getForEntity("http://localhost:" + port + "/api/post/" + saved_group.getName() + "/groupPosts", List.class);

                assertNotNull(response);
                assertNull(response.getBody());
                assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
        }
}
