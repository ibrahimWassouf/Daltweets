package com.csci3130.group04.Daltweets;

import java.time.LocalDateTime;
import java.util.Map;

import com.csci3130.group04.Daltweets.utils.SignUpRequestDTO;
import org.junit.jupiter.api.AfterEach;
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

import com.csci3130.group04.Daltweets.model.Login;
import com.csci3130.group04.Daltweets.model.User;
import com.csci3130.group04.Daltweets.model.User.Role;
import com.csci3130.group04.Daltweets.repository.LoginRepository;
import com.csci3130.group04.Daltweets.repository.UserRepository;
import com.csci3130.group04.Daltweets.service.Implementation.LoginServiceImpl;

import static org.junit.jupiter.api.Assertions.*;

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
    userRepository.deleteAll();
  }
  
  @Test
  public void test_valid_login() throws Exception{
    
    User user = new User(1, "my bio", "me", "me@dal.ca", LocalDateTime.now(), false, Role.SUPERADMIN, User.Status.ONLINE);
    User saved_user = userRepository.save(user);

    Login login = new Login(1,"admin", "Password1!", "security", "answer", saved_user);
    loginRepository.save(login);
    
    Map<String, String> requestBody = Map.ofEntries(Map.entry("username", "admin"), Map.entry("password", "Password1!"));

    ResponseEntity<User> response = this.restTemplate.postForEntity("http://localhost:" + port + "/api/login/", requestBody, User.class);

    assertNotNull(response.getBody());
    assertEquals(response.getBody().getUsername(), "me");
  }

  @Test
  public void test_user_not_found_login() throws Exception{
    Login login = new Login(1,"admin", "Password1!", "security", "answer", null);
    loginRepository.save(login);

    Map<String, String> requestBody = Map.ofEntries(Map.entry("username", "admin"), Map.entry("password", "Password1!"));
    ResponseEntity<String> response = this.restTemplate.postForEntity("http://localhost:" + port + "/api/login/", requestBody, String.class);

    assertNull(response.getBody());
    assertEquals("404 NOT_FOUND", response.getStatusCode().toString());
  }

  @Test
  public void test_incorrect_password() throws Exception{
    
    User user = new User(1, "my bio", "me", "me@dal.ca", LocalDateTime.now(), false, Role.SUPERADMIN, User.Status.ONLINE);
    userRepository.save(user);
    User saved_user = userRepository.save(user);

    Login login = new Login(1,"admin", "Password1!", "security", "answer", saved_user);
    loginRepository.save(login);

    Map<String, String> requestBody = Map.ofEntries(Map.entry("username", "admin"), Map.entry("password", "not password"));
    ResponseEntity<User> response = this.restTemplate.postForEntity("http://localhost:" + port + "/api/login/", requestBody, User.class);

    assertNull(response.getBody());
    assertEquals("400 BAD_REQUEST", response.getStatusCode().toString());
  }

  @Test void get_security_question() throws Exception{
    User user = new User(1, "my bio", "me", "me@dal.ca", LocalDateTime.now(), false, Role.SUPERADMIN, User.Status.ONLINE);
    userRepository.save(user);
    User saved_user = userRepository.save(user);

    Login login = new Login(1,"admin", "Password1!", "security question", "answer", saved_user);
    login = loginRepository.save(login);

    ResponseEntity<String> response = this.restTemplate.getForEntity("http://localhost:" + port + "/api/login/get-security-question/" + login.getUsername(), String.class);

    assertEquals("security question", response.getBody());
    assertEquals("200 OK", response.getStatusCode().toString());
  }

  @Test
  public void test_change_password() throws Exception{
    User user = new User(1, "my bio", "me", "me@dal.ca", LocalDateTime.now(), false, Role.SUPERADMIN, User.Status.ONLINE);
    userRepository.save(user);
    User saved_user = userRepository.save(user);

    Login login = new Login(1,"admin", "Password1!", "security", "answer", saved_user);
    loginRepository.save(login);

    Map<String, String> requestBody = Map.ofEntries(
        Map.entry("username", "admin"), 
        Map.entry("password", "Password1!"), 
        Map.entry("security-answer", "answer"), 
        Map.entry("new-password", "new password"));

    ResponseEntity<String> response = this.restTemplate.postForEntity("http://localhost:" + port + "/api/login/change-password", requestBody, String.class);

    Login queriedLogin = loginService.getLogin("admin");

    assertEquals("Password updated", response.getBody());
    assertEquals("new password", queriedLogin.getPassword());
  }

  @Test
  public void test_create_login() {
    User user = new User(1, "my bio", "me", "me@dal.ca", LocalDateTime.now(), false, Role.SUPERADMIN, User.Status.ONLINE);
    User saved_user = userRepository.save(user);

    Login login = new Login(1,"me@dal.ca", "Password1!", "security", "answer", saved_user);

    boolean result = loginService.createLogin(login);

    assertTrue(result);
  }

  @Test
  public void test_create_login_with_null() {
    boolean result = loginService.createLogin(null);
    assertFalse(result);
  }

  @Test
  public void test_create_login_with_null_user() {
    User saved_user = null;

    Login login = new Login(1,"me@dal.ca", "Password1!", "security", "answer", saved_user);

    boolean result = loginService.createLogin(login);

    assertFalse(result);
  }

  @Test
  public void test_create_login_with_wrong_password() {
    User user = new User(1, "my bio", "me", "me@dal.ca", LocalDateTime.now(), false, Role.SUPERADMIN, User.Status.ONLINE);
    User saved_user = userRepository.save(user);

    Login login = new Login(1,"me@dal.ca", "Password1", "security", "answer", saved_user);

    boolean result = loginService.createLogin(login);

    assertFalse(result);
  }
}

