package com.csci3130.group04.Daltweets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.csci3130.group04.Daltweets.model.Post;
import com.csci3130.group04.Daltweets.model.User;
import com.csci3130.group04.Daltweets.model.User.Role;
import com.csci3130.group04.Daltweets.repository.PostRepository;
import com.csci3130.group04.Daltweets.service.Implementation.PostServiceImpl;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class PostServiceUnitTests {

  @Mock
  PostRepository postRepository;

  @InjectMocks
  PostServiceImpl postService = new PostServiceImpl();


  @Test
  void test_create_post(){
    Post post = new Post();
    post.setUser(new User(1, "my bio", "me", "me@email", LocalDateTime.now(), false, Role.SUPERADMIN, User.Status.ONLINE));
    post.setText("This is the post text");
    
    Mockito.when(postRepository.save(post)).thenReturn(post);
    Post returnedVal = postService.createPost(post);

    assertEquals(post.getText(), returnedVal.getText());
    assertNotNull(returnedVal.getDateCreated());
  }

  @Test
  void test_create_post_with_null_user(){
    Post post = new Post();
    post.setText("Some text");

    Post returnedVal = postService.createPost(post);

    assertNull(returnedVal);
  }

  @Test
  void test_create_post_with_null_text(){
    Post post = new Post();
    post.setUser(new User(1, "my bio", "me", "me@email", LocalDateTime.now(), false, Role.SUPERADMIN, User.Status.ONLINE));

    Post returnedVal = postService.createPost(post);

    assertNull(returnedVal);
  }

  @Test
  void test_get_all_posts_by_user(){
    User user = new User(1, "my bio", "me", "me@email", LocalDateTime.now(), false, Role.SUPERADMIN, User.Status.ONLINE);
    Post post1 = new Post(1, user, "my first post", LocalDateTime.now(), false, false);
    Post post2 = new Post(2, user, "my second post", LocalDateTime.now(), false, false);
    Post post3 = new Post(3, user, "my third post", LocalDateTime.now(), false, false);

    Post sentPost1 = postService.createPost(post1);
    Post sentPost2 = postService.createPost(post2);
    Post sentPost3 = postService.createPost(post3);
    List<Post> list = new ArrayList<>();
    list.add(post1);
    list.add(post2);
    list.add(post3);


    Mockito.when(postRepository.findPostsByUser(user)).thenReturn(list);
    List<Post> queriedPosts = postService.getPostsByUser(user);
    assertEquals("my first post", queriedPosts.get(0).getText());
    assertEquals("my second post", queriedPosts.get(1).getText());
    assertEquals("my third post", queriedPosts.get(2).getText());
  }

  @Test
  void test_get_all_post_by_users(){
    User user = new User(1, "my bio", "me", "me@email", LocalDateTime.now(), false, Role.SUPERADMIN, User.Status.ONLINE);
    User user2 = new User(2, "it's me", "you", "you@email", LocalDateTime.now(), false, Role.SUPERADMIN, User.Status.ONLINE);
    User user3 = new User(3, "that's me", "they", "they@email", LocalDateTime.now(), false, Role.SUPERADMIN, User.Status.ONLINE);

    List<User> userList = new ArrayList<>();
    userList.add(user);
    userList.add(user2);
    userList.add(user3);

    Post post1 = new Post(1, user, "my first post", LocalDateTime.now(), false, false);
    Post post2 = new Post(2, user2, "my second post", LocalDateTime.now(), false, false);
    Post post3 = new Post(3, user3, "my third post", LocalDateTime.now(), false, false);

    Post sentPost1 = postService.createPost(post1);
    Post sentPost2 = postService.createPost(post2);
    Post sentPost3 = postService.createPost(post3);
    List<Post> postList = new ArrayList<>();

    postList.add(post1);
    postList.add(post2);
    postList.add(post3);

    Mockito.when(postRepository.findPostsByUserIn(userList)).thenReturn(postList);
    List<Post> queriedPosts = postService.getPostsByUsers(userList);

    assertEquals(queriedPosts, postList);
  }

  @Test
  void test_get_all_posts_by_users_with_null_in_user_list(){
    
    User user = new User(1, "my bio", "me", "me@email", LocalDateTime.now(), false, Role.SUPERADMIN, User.Status.ONLINE);
    User user2 = new User();
    User user3 = new User(3, "that's me", "they", "they@email", LocalDateTime.now(), false, Role.SUPERADMIN, User.Status.ONLINE);

    List<User> userList = new ArrayList<>();
    userList.add(user);
    userList.add(user2);
    userList.add(user3);

    Post post1 = new Post(1, user, "my first post", LocalDateTime.now(), false, false);
    Post post2 = new Post(2, user2, "my second post", LocalDateTime.now(), false, false);
    Post post3 = new Post(3, user3, "my third post", LocalDateTime.now(), false, false);

    Post sentPost1 = postService.createPost(post1);
    Post sentPost2 = postService.createPost(post2);
    Post sentPost3 = postService.createPost(post3);
    List<Post> postList = new ArrayList<>();

    postList.add(post1);
    postList.add(null);
    postList.add(post3);

    Mockito.when(postRepository.findPostsByUserIn(userList)).thenReturn(postList);
    List<Post> queriedPosts = postService.getPostsByUsers(userList);

    assertEquals(queriedPosts, postList);
  
  }
}

