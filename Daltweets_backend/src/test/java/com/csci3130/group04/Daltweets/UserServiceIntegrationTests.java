package com.csci3130.group04.Daltweets;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.csci3130.group04.Daltweets.model.Followers;
import com.csci3130.group04.Daltweets.model.Group;
import com.csci3130.group04.Daltweets.model.Login;
import com.csci3130.group04.Daltweets.repository.FollowersRepository;
import com.csci3130.group04.Daltweets.repository.LoginRepository;
import com.csci3130.group04.Daltweets.service.Implementation.FollowersServiceImpl;
import com.csci3130.group04.Daltweets.service.Implementation.LoginServiceImpl;
import com.csci3130.group04.Daltweets.utils.SignUpRequestDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import com.csci3130.group04.Daltweets.model.User;
import com.csci3130.group04.Daltweets.model.User.Role;
import com.csci3130.group04.Daltweets.repository.UserRepository;
import com.csci3130.group04.Daltweets.service.Implementation.UserServiceImplementation;

@SpringBootTest(classes = DaltweetsApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
class UserServiceIntegrationTests {
  
  @Autowired
  private UserServiceImplementation userService;

  @Autowired
  private UserRepository userRepository;
  @Autowired
  private LoginRepository loginRepository;
  @Autowired
  private FollowersRepository followersRepository;

  @Autowired
  TestRestTemplate restTemplate;

  @Autowired
  LoginServiceImpl loginService;
  @Autowired
  FollowersServiceImpl followersService;
  @LocalServerPort
  private int port;

 @AfterEach
  void teardown(){
     followersRepository.deleteAll();
     loginRepository.deleteAll();
     userRepository.deleteAll();
  }

  @Test
  void test_get_by_username_is_unique(){
    User user = new User(1,"checkbio","Name","firstmail", LocalDateTime.now(),false, User.Role.SUPERADMIN, User.Status.ONLINE);
    User user2 = new User(1,"checkbio","Name","mail2", LocalDateTime.now(),false, User.Role.SUPERADMIN, User.Status.ONLINE);

    userService.createUser(user);
    userService.createUser(user2);

    User queriedUser = userService.getUserByName("Name");

    assertEquals("Name", queriedUser.getUsername());
    assertEquals("firstmail", queriedUser.getEmail());

  }

  @Test
  void test_get_recommended(){

    User user = new User(1,"checkbio","Name","firstmail", LocalDateTime.now(),false, User.Role.SUPERADMIN, User.Status.ONLINE);
    User user2 = new User(2,"checkbio2","Name2","mail2", LocalDateTime.now(),false, User.Role.SUPERADMIN, User.Status.ONLINE);
    User user3 = new User(3,"checkbio3","Name3","mail3", LocalDateTime.now(),false, User.Role.SUPERADMIN, User.Status.ONLINE);
    User user4 = new User(4,"checkbio4","Name4","mail4", LocalDateTime.now(),false, User.Role.SUPERADMIN, User.Status.ONLINE);

    List<User> list = new ArrayList<>();
    list.add(user);
    list.add(user2);
    list.add(user3);
    list.add(user4);
    userRepository.saveAll(list);

    List<User> queriedUsers = userService.getRecommendedUsers(user.getUsername());

   assertEquals(list.get(1).getUsername(), queriedUsers.get(0).getUsername());
   assertEquals(list.get(2).getUsername(), queriedUsers.get(1).getUsername());
   assertEquals(list.get(3).getUsername(), queriedUsers.get(2).getUsername());

  }

  @Test
  void test_add_existing_user(){
    User admin = new User(1,"checkbio","admin","firstmail", LocalDateTime.now(),false, User.Role.SUPERADMIN, User.Status.ONLINE);
    admin = userRepository.save(admin);
    User user = new User(admin.getId()+1,"checkbio","Name","firstmail", LocalDateTime.now(),false, User.Role.USER, User.Status.DEACTIVATED);
    user = userRepository.save(user);
    
    Map<String, String> requestBody = Map.ofEntries(Map.entry("adminName","admin"), Map.entry("username", "Name"));
    ResponseEntity<String> response = this.restTemplate.postForEntity("http://localhost:" + port + "/api/user/activate",requestBody, String.class);

    assertEquals("true", response.getBody());
    assertEquals("200 OK", response.getStatusCode().toString());
  }

  @Test
  void test_add_existing_user_with_no_permission()
  {
    User admin = new User(1,"checkbio","admin","firstmail", LocalDateTime.now(),false, User.Role.USER, User.Status.ONLINE);
    admin = userRepository.save(admin);
    User user = new User(admin.getId()+1,"checkbio","Name","firstmail", LocalDateTime.now(),false, User.Role.USER, User.Status.DEACTIVATED);
    user = userRepository.save(user);

    Map<String, String> requestBody = Map.ofEntries(Map.entry("adminName","admin"), Map.entry("username", ""));
    
    assertThrows(Throwable.class, () -> {this.restTemplate.postForEntity("http://localhost:" + port + "/api/user/activate",requestBody, String.class);});
  }

  @Test
  void test_add_existing_user_with_empty_name()
  {
    User admin = new User(1,"checkbio","admin","firstmail", LocalDateTime.now(),false, User.Role.SUPERADMIN, User.Status.ONLINE);
    admin = userRepository.save(admin);

    Map<String, String> requestBody = Map.ofEntries(Map.entry("adminName","admin"), Map.entry("username", ""));
    ResponseEntity<String> response = this.restTemplate.postForEntity("http://localhost:" + port + "/api/user/activate",requestBody, String.class);

    assertEquals("false", response.getBody());
    assertEquals(HttpStatus.NOT_IMPLEMENTED, response.getStatusCode());
  }

  @Test
  void test_add_existing_user_with_non_existing_user()
  {
    User admin = new User(1,"checkbio","admin","firstmail", LocalDateTime.now(),false, User.Role.SUPERADMIN, User.Status.ONLINE);
    admin = userRepository.save(admin);

    Map<String, String> requestBody = Map.ofEntries(Map.entry("adminName","admin"), Map.entry("username", "Name"));
    ResponseEntity<String> response = this.restTemplate.postForEntity("http://localhost:" + port + "/api/user/activate",requestBody, String.class);

    assertEquals("false", response.getBody());
    assertEquals(HttpStatus.NOT_IMPLEMENTED, response.getStatusCode());
  }


  @Test
  void test_delete_existing_user(){
    User admin = new User(1,"checkbio","admin","firstmail", LocalDateTime.now(),false, User.Role.SUPERADMIN, User.Status.ONLINE);
    admin = userRepository.save(admin);
    User user = new User(admin.getId()+1,"checkbio","Name","firstmail", LocalDateTime.now(),false, User.Role.USER, User.Status.ONLINE);
    user = userRepository.save(user);
    
    Map<String, String> requestBody = Map.ofEntries(Map.entry("adminName","admin"), Map.entry("username", "Name"));
    ResponseEntity<String> response = this.restTemplate.postForEntity("http://localhost:" + port + "/api/user/activate",requestBody, String.class);

    assertEquals("true", response.getBody());
    assertEquals("200 OK", response.getStatusCode().toString());
  }

  @Test
  void test_delete_existing_user_with_no_permission()
  {
    User admin = new User(1,"checkbio","admin","firstmail", LocalDateTime.now(),false, User.Role.USER, User.Status.ONLINE);
    admin = userRepository.save(admin);
    User user = new User(admin.getId()+1,"checkbio","Name","firstmail", LocalDateTime.now(),false, User.Role.USER, User.Status.ONLINE);
    user = userRepository.save(user);

    Map<String, String> requestBody = Map.ofEntries(Map.entry("adminName","admin"), Map.entry("username", ""));
    
    assertThrows(Throwable.class, () -> {this.restTemplate.postForEntity("http://localhost:" + port + "/api/user/deactivate",requestBody, String.class);});
  }

  @Test
  void test_delete_existing_user_with_empty_name()
  {
    User admin = new User(1,"checkbio","admin","firstmail", LocalDateTime.now(),false, User.Role.SUPERADMIN, User.Status.ONLINE);
    admin = userRepository.save(admin);
    User user = new User(admin.getId()+1,"checkbio","Name","firstmail", LocalDateTime.now(),false, User.Role.USER, User.Status.ONLINE);
    user = userRepository.save(user);

    Map<String, String> requestBody = Map.ofEntries(Map.entry("adminName","admin"), Map.entry("username", ""));
    ResponseEntity<String> response = this.restTemplate.postForEntity("http://localhost:" + port + "/api/user/deactivate",requestBody, String.class);

    assertEquals("false", response.getBody());
    assertEquals(HttpStatus.NOT_IMPLEMENTED, response.getStatusCode());
  }

  @Test
  void test_delete_existing_user_with_non_existing_user()
  {
    User admin = new User(1,"checkbio","admin","firstmail", LocalDateTime.now(),false, User.Role.SUPERADMIN, User.Status.ONLINE);
    admin = userRepository.save(admin);

    Map<String, String> requestBody = Map.ofEntries(Map.entry("adminName","admin"), Map.entry("username", "Name"));
    ResponseEntity<String> response = this.restTemplate.postForEntity("http://localhost:" + port + "/api/user/deactivate",requestBody, String.class);

    assertEquals("false", response.getBody());
    assertEquals(HttpStatus.NOT_IMPLEMENTED, response.getStatusCode());
  }
  @Test
  public void test_create_user() {
     User user = new User(1,"checkbio","Name","firstmail@dal.ca", LocalDateTime.now(),false, User.Role.SUPERADMIN, User.Status.ONLINE);

     Login login = new Login();
     login.setPassword("Password1!");
     login.setSecurityQuestion("security");
     login.setSecurityAnswer("answer");

     SignUpRequestDTO requestBody = new SignUpRequestDTO(user,login);

     ResponseEntity<User> response = this.restTemplate.postForEntity("http://localhost:" + port + "/api/user/signup",requestBody, User.class);

     assertNotNull(response);
     assertEquals(user.getUsername(),response.getBody().getUsername());
  }
  @Test
  public void test_create_user_with_null() {
     User user = new User(1,"checkbio",null,"firstmail@dal.ca", LocalDateTime.now(),false, User.Role.SUPERADMIN, User.Status.ONLINE);

     Login login = new Login();
     login.setPassword("Password1!");
     login.setSecurityQuestion("security");
     login.setSecurityAnswer("answer");

     SignUpRequestDTO requestBody = new SignUpRequestDTO(user,login);

     assertThrows(Throwable.class,()->this.restTemplate.postForEntity("http://localhost:" + port + "/api/user/signup",requestBody, User.class));
  }
  @Test
  public void test_recommend_user() {
      User user = new User(1, "my bio", "me", "me@email", LocalDateTime.now(), false, Role.SUPERADMIN, User.Status.ONLINE);
      User user2 = new User(2, "it's me", "you", "you@email", LocalDateTime.now(), false, Role.SUPERADMIN, User.Status.ONLINE);
      User user3 = new User(3, "that's me", "they", "they@email", LocalDateTime.now(), false, Role.SUPERADMIN, User.Status.ONLINE);
      User saved_user = userRepository.save(user);
      User saved_user2 = userRepository.save(user2);
      User saved_user3 = userRepository.save(user3);

      Followers newFollower = followersService.addFollower(saved_user2, saved_user);

      Map<String,String> requestBody = Map.ofEntries(Map.entry("username","me"));
      ResponseEntity<List> response = this.restTemplate.postForEntity("http://localhost:" + port + "/api/user/recommended-users",requestBody,List.class);
      int expect_size = 1;

      assertNotNull(response);
      assertEquals(1,response.getBody().size());
  }
  @Test
  public void test_recommend_user_with_no_follower() {
     User user = new User(1, "my bio", "me", "me@email", LocalDateTime.now(), false, Role.SUPERADMIN, User.Status.ONLINE);
     User user2 = new User(2, "it's me", "you", "you@email", LocalDateTime.now(), false, Role.SUPERADMIN, User.Status.ONLINE);
     User user3 = new User(3, "that's me", "they", "they@email", LocalDateTime.now(), false, Role.SUPERADMIN, User.Status.ONLINE);
     User saved_user = userRepository.save(user);
     User saved_user2 = userRepository.save(user2);
     User saved_user3 = userRepository.save(user3);

     Map<String,String> requestBody = Map.ofEntries(Map.entry("username","me"));
     ResponseEntity<List> response = this.restTemplate.postForEntity("http://localhost:" + port + "/api/user/recommended-users",requestBody,List.class);
     int expect_size = 2;

     assertNotNull(response);
     assertEquals(expect_size,response.getBody().size());
  }

  @Test
  public void test_recommend_user_with_full_follower() {
     User user = new User(1, "my bio", "me", "me@email", LocalDateTime.now(), false, Role.SUPERADMIN, User.Status.ONLINE);
     User user2 = new User(2, "it's me", "you", "you@email", LocalDateTime.now(), false, Role.SUPERADMIN, User.Status.ONLINE);
     User saved_user = userRepository.save(user);
     User saved_user2 = userRepository.save(user2);

     Followers newFollower = followersService.addFollower(saved_user2, saved_user);

     Map<String,String> requestBody = Map.ofEntries(Map.entry("username","me"));
     ResponseEntity<List> response = this.restTemplate.postForEntity("http://localhost:" + port + "/api/user/recommended-users",requestBody,List.class);
     int expect_size = 0;

     assertNotNull(response);
     assertEquals(expect_size,response.getBody().size());
 }
  @Test
  public void test_recommend_user_with_null() {
     User user = new User(1, "my bio", null, "me@email", LocalDateTime.now(), false, Role.SUPERADMIN, User.Status.ONLINE);
     User user2 = new User(2, "it's me", "you", "you@email", LocalDateTime.now(), false, Role.SUPERADMIN, User.Status.ONLINE);
     User user3 = new User(3, "that's me", "they", "they@email", LocalDateTime.now(), false, Role.SUPERADMIN, User.Status.ONLINE);
     User saved_user = userRepository.save(user);
     User saved_user2 = userRepository.save(user2);
     User saved_user3 = userRepository.save(user3);

     Followers newFollower = followersService.addFollower(saved_user2, saved_user);

     Map<String,String> requestBody = Map.ofEntries(Map.entry("username","me"));

     assertThrows(Throwable.class,()->this.restTemplate.postForEntity("http://localhost:" + port + "/api/user/recommended-users",requestBody,List.class));
  }

  @Test
  public void test_get_user_profile() {
     User user = new User(1, "my bio", "Name", "me@email", LocalDateTime.now(), false, Role.SUPERADMIN, User.Status.ONLINE);
     User saved_user = userRepository.save(user);
     ResponseEntity<User> response = this.restTemplate.getForEntity("http://localhost:" + port + "/api/user/" + saved_user.getUsername() +"/profile",User.class);

     assertNotNull(response);
     assertEquals(saved_user.getUsername(),response.getBody().getUsername());
  }

  @Test
  public void test_get_user_profile_with_NULL() {
     User user = new User(1, "my bio", null, "me@email", LocalDateTime.now(), false, Role.SUPERADMIN, User.Status.ONLINE);
     User saved_user = userRepository.save(user);

     ResponseEntity<User> response = this.restTemplate.getForEntity("http://localhost:" + port + "/api/user/" + saved_user.getUsername() +"/profile",User.class);

     assertNull(response.getBody());
  }

  @Test
  public void test_get_user_profile_with_no_user() {
     User user = new User(1, "my bio", "Name", "me@email", LocalDateTime.now(), false, Role.SUPERADMIN, User.Status.ONLINE);

     ResponseEntity<User> response = this.restTemplate.getForEntity("http://localhost:" + port + "/api/user/" + user.getUsername() +"/profile",User.class);

     assertNull(response.getBody());
  }
  @Test
  public void test_update_user() {
     User user = new User(1, "my bio", "Name", "me@email", LocalDateTime.now(), false, Role.SUPERADMIN, User.Status.ONLINE);
     User saved_user = userRepository.save(user);
     User change_user = saved_user;
     change_user.setStatus(User.Status.OFFLINE);

     ResponseEntity<User> response = this.restTemplate.exchange("http://localhost:" + port + "/api/user/update", HttpMethod.PUT,new HttpEntity<>(change_user),User.class);

     assertEquals(change_user.getStatus(),response.getBody().getStatus());
  }

  @Test
  public void test_update_user_with_null() {
     User user = new User(1, "my bio", null, "me@email", LocalDateTime.now(), false, Role.SUPERADMIN, User.Status.ONLINE);
     User saved_user = userRepository.save(user);
     User change_user = saved_user;
     change_user.setStatus(User.Status.OFFLINE);

     assertThrows(Throwable.class,()->this.restTemplate.exchange("http://localhost:" + port + "/api/user/update", HttpMethod.PUT,new HttpEntity<>(change_user),User.class));
 }

 @Test
 public void test_update_user_with_not_exist_user() {
     User user = new User(1, "my bio", null, "me@email", LocalDateTime.now(), false, Role.SUPERADMIN, User.Status.ONLINE);
     User change_user = user;
     change_user.setStatus(User.Status.OFFLINE);

     assertThrows(Throwable.class,()->this.restTemplate.exchange("http://localhost:" + port + "/api/user/update", HttpMethod.PUT,new HttpEntity<>(change_user),User.class));
 }
}
