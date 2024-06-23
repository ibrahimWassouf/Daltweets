package com.csci3130.group04.Daltweets;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.csci3130.group04.Daltweets.model.User;
import com.csci3130.group04.Daltweets.model.User.Role;
import com.csci3130.group04.Daltweets.repository.UserRepository;
import com.csci3130.group04.Daltweets.service.Implementation.UserServiceImplementation;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@Transactional
class UserServiceIntegrationTests {
  
  @Autowired
  private UserServiceImplementation userService;

  @Autowired
  private UserRepository userRepository;

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
}
