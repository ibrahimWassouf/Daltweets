package com.csci3130.group04.Daltweets;

import java.time.LocalDateTime;
import java.util.Map;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.csci3130.group04.Daltweets.controller.LoginController;
import com.csci3130.group04.Daltweets.model.Login;
import com.csci3130.group04.Daltweets.model.User;
import com.csci3130.group04.Daltweets.model.User.Role;
import com.csci3130.group04.Daltweets.repository.LoginRepository;
import com.csci3130.group04.Daltweets.repository.UserRepository;
import com.csci3130.group04.Daltweets.service.Implementation.LoginServiceImpl;
import com.csci3130.group04.Daltweets.service.Implementation.UserServiceImplementation;

@SpringBootTest(classes = DaltweetsApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
class LoginServiceIntegrationTests {
  
  @LocalServerPort
  private int port;
  
  @Autowired
  private LoginServiceImpl loginService;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private LoginRepository loginRepository;
  
  @Autowired
  TestRestTemplate restTemplate;

  @AfterEach
  void teardown(){
    loginRepository.deleteAll();
  }
  
  @Test
  public void test_valid_login() throws Exception{
    
    User user = new User(1, "my bio", "me", "me@email", LocalDateTime.now(), false, Role.SUPERADMIN, User.Status.ONLINE);
    User saved_user = userRepository.save(user);

    Login login = new Login(1,"admin", "password", "security", "answer", saved_user);
    loginRepository.save(login);
    
    Map<String, String> requestBody = Map.ofEntries(Map.entry("username", "admin"), Map.entry("password", "password"));

    ResponseEntity<String> response = this.restTemplate.postForEntity("http://localhost:" + port + "/api/login/", requestBody, String.class);
    
    assertEquals("User Authenticated", response.getBody());
  }

  @Test
  public void test_user_not_found_login() throws Exception{
    Login login = new Login(1,"admin", "password", "security", "answer", null);
    loginRepository.save(login);

    Map<String, String> requestBody = Map.ofEntries(Map.entry("username", "admin"), Map.entry("password", "password"));
    ResponseEntity<String> response = this.restTemplate.postForEntity("http://localhost:" + port + "/api/login/", requestBody, String.class);

    assertEquals("User not found", response.getBody());
    assertEquals("404 NOT_FOUND", response.getStatusCode().toString());
  }

  @Test
  public void test_incorrect_password() throws Exception{
    
    User user = new User(1, "my bio", "me", "me@email", LocalDateTime.now(), false, Role.SUPERADMIN, User.Status.ONLINE);
    userRepository.save(user);
    User saved_user = userRepository.save(user);

    Login login = new Login(1,"admin", "password", "security", "answer", saved_user);
    loginRepository.save(login);

    Map<String, String> requestBody = Map.ofEntries(Map.entry("username", "admin"), Map.entry("password", "not password"));
    ResponseEntity<String> response = this.restTemplate.postForEntity("http://localhost:" + port + "/api/login/", requestBody, String.class);

    assertEquals("Incorrect Password", response.getBody());
    assertEquals("400 BAD_REQUEST", response.getStatusCode().toString());
  }

  @Test void get_security_question() throws Exception{
    User user = new User(1, "my bio", "me", "me@email", LocalDateTime.now(), false, Role.SUPERADMIN, User.Status.ONLINE);
    userRepository.save(user);
    User saved_user = userRepository.save(user);

    Login login = new Login(1,"admin", "password", "security question", "answer", saved_user);
    loginRepository.save(login);

    Map<String, String> requestBody = Map.ofEntries(Map.entry("username", "admin"), Map.entry("password", "password"));
    ResponseEntity<String> response = this.restTemplate.postForEntity("http://localhost:" + port + "/api/login/get-security-question", requestBody, String.class);

    assertEquals("security question", response.getBody());
    assertEquals("200 OK", response.getStatusCode().toString());
  }

  @Test
  public void test_change_password() throws Exception{
    User user = new User(1, "my bio", "me", "me@email", LocalDateTime.now(), false, Role.SUPERADMIN, User.Status.ONLINE);
    userRepository.save(user);
    User saved_user = userRepository.save(user);

    Login login = new Login(1,"admin", "password", "security", "answer", saved_user);
    loginRepository.save(login);

    Map<String, String> requestBody = Map.ofEntries(
        Map.entry("username", "admin"), 
        Map.entry("password", "password"), 
        Map.entry("security-answer", "answer"), 
        Map.entry("new-password", "new password"));

    ResponseEntity<String> response = this.restTemplate.postForEntity("http://localhost:" + port + "/api/login/change-password", requestBody, String.class);

    Login queriedLogin = loginService.getLogin("admin");

    assertEquals("Password updated", response.getBody());
    assertEquals("new password", queriedLogin.getPassword());
  }
}

