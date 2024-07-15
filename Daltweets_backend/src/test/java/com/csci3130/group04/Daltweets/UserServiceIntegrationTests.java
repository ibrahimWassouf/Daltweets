package com.csci3130.group04.Daltweets;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import com.csci3130.group04.Daltweets.model.User;
import com.csci3130.group04.Daltweets.model.User.Role;
import com.csci3130.group04.Daltweets.repository.UserRepository;
import com.csci3130.group04.Daltweets.service.Implementation.UserServiceImplementation;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = DaltweetsApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
class UserServiceIntegrationTests {
  
  @Autowired
  private UserServiceImplementation userService;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  TestRestTemplate restTemplate;

  @LocalServerPort
  private int port;

 @AfterEach
  void teardown(){
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
  void test_change_status_with_accept_user() {
     User admin = new User(1,"checkbio","admin","firstmail", LocalDateTime.now(),false, User.Role.SUPERADMIN, User.Status.ONLINE);
     admin = userRepository.save(admin);

     User user = new User(1,"checkbio","Name","firstmail", LocalDateTime.now(),false, User.Role.USER, User.Status.PENDING);
     User saved_user = userRepository.save(user);

     Map<String,String> requestBody = Map.ofEntries(Map.entry("adminname","admin"),Map.entry("username","Name"),Map.entry("status","ACTIVATED"));
     ResponseEntity<String> response = this.restTemplate.postForEntity("http://localhost:" + port + "/api/user/changeStatus",requestBody,String.class);

     String result = "Success ACTIVATED " + saved_user.getUsername();

     assertNotNull(response);
     assertEquals(result,response.getBody());
     assertEquals(HttpStatus.OK,response.getStatusCode());
  }

  @Test
  void test_change_status_with_reject_user() {
      User admin = new User(1,"checkbio","admin","firstmail", LocalDateTime.now(),false, User.Role.SUPERADMIN, User.Status.ONLINE);
      admin = userRepository.save(admin);

      User user = new User(1,"checkbio","Name","firstmail", LocalDateTime.now(),false, User.Role.USER, User.Status.PENDING);
      User saved_user = userRepository.save(user);

      Map<String,String> requestBody = Map.ofEntries(Map.entry("adminname","admin"),Map.entry("username","Name"),Map.entry("status","DEACTIVATED"));
      ResponseEntity<String> response = this.restTemplate.postForEntity("http://localhost:" + port + "/api/user/changeStatus",requestBody,String.class);

      String result = "Success DEACTIVATED " + saved_user.getUsername();

      assertNotNull(response);
      assertEquals(result,response.getBody());
      assertEquals(HttpStatus.OK,response.getStatusCode());
  }

  @Test
  void test_change_status_with_NonExistUser() {
      User admin = new User(1,"checkbio","admin","firstmail", LocalDateTime.now(),false, User.Role.SUPERADMIN, User.Status.ONLINE);
      admin = userRepository.save(admin);

      User user = new User(1,"checkbio","Name","firstmail", LocalDateTime.now(),false, User.Role.USER, User.Status.PENDING);


      Map<String,String> requestBody = Map.ofEntries(Map.entry("adminname","admin"),Map.entry("username","Name"),Map.entry("status","DEACTIVATED"));
      ResponseEntity<String> response = this.restTemplate.postForEntity("http://localhost:" + port + "/api/user/changeStatus",requestBody,String.class);

      assertNotNull(response);
      assertNull(response.getBody());
      assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
  }

  @Test
  void test_change_status_with_NonAdmin() {
     User admin = new User(1,"checkbio","admin","firstmail", LocalDateTime.now(),false, User.Role.USER, User.Status.ONLINE);
     admin = userRepository.save(admin);

     User user = new User(1,"checkbio","Name","firstmail", LocalDateTime.now(),false, User.Role.USER, User.Status.PENDING);
     User saved_user = userRepository.save(user);


     Map<String,String> requestBody = Map.ofEntries(Map.entry("adminname","admin"),Map.entry("username","Name"),Map.entry("status","DEACTIVATED"));
     ResponseEntity<String> response = this.restTemplate.postForEntity("http://localhost:" + port + "/api/user/changeStatus",requestBody,String.class);

     assertNotNull(response);
     assertNull(response.getBody());
     assertEquals(HttpStatus.BAD_REQUEST,response.getStatusCode());
  }

  @Test
  void test_change_status_with_empty_name() {
      User admin = new User(1,"checkbio","admin","firstmail", LocalDateTime.now(),false, Role.SUPERADMIN, User.Status.ONLINE);
      admin = userRepository.save(admin);

      User user = new User(1,"checkbio","Name","firstmail", LocalDateTime.now(),false, User.Role.USER, User.Status.PENDING);
      User saved_user = userRepository.save(user);


      Map<String,String> requestBody = Map.ofEntries(Map.entry("adminname","admin"),Map.entry("username"," "),Map.entry("status","DEACTIVATED"));
      ResponseEntity<String> response = this.restTemplate.postForEntity("http://localhost:" + port + "/api/user/changeStatus",requestBody,String.class);

      assertNotNull(response);
      assertNull(response.getBody());
      assertEquals(HttpStatus.BAD_REQUEST,response.getStatusCode());
  }

  void test_change_status_with_wrong_status() {
      User admin = new User(1,"checkbio","admin","firstmail", LocalDateTime.now(),false, Role.SUPERADMIN, User.Status.ONLINE);
      admin = userRepository.save(admin);

      User user = new User(1,"checkbio","Name","firstmail", LocalDateTime.now(),false, User.Role.USER, User.Status.PENDING);
      User saved_user = userRepository.save(user);


      Map<String,String> requestBody = Map.ofEntries(Map.entry("adminname","admin"),Map.entry("username","Name"),Map.entry("status","WRONG"));
      ResponseEntity<String> response = this.restTemplate.postForEntity("http://localhost:" + port + "/api/user/changeStatus",requestBody,String.class);

      assertNotNull(response);
      assertNull(response.getBody());
      assertEquals(HttpStatus.BAD_REQUEST,response.getStatusCode());
  }
}
