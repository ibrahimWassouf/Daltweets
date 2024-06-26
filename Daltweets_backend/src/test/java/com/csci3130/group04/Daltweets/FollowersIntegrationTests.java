package com.csci3130.group04.Daltweets;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

import com.csci3130.group04.Daltweets.model.Followers;
import com.csci3130.group04.Daltweets.model.Followers.Status;
import com.csci3130.group04.Daltweets.model.User;
import com.csci3130.group04.Daltweets.model.User.Role;
import com.csci3130.group04.Daltweets.repository.FollowersRepository;
import com.csci3130.group04.Daltweets.repository.UserRepository;
import com.csci3130.group04.Daltweets.service.FollowersService;


@SpringBootTest(classes = DaltweetsApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class FollowersIntegrationTests {
    
    @LocalServerPort
    private int port;
 
    @Autowired 
    private FollowersService followersService;

    @Autowired
    private FollowersRepository followersRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    TestRestTemplate restTemplate;
   
    @AfterEach
    void setup(){
      followersRepository.deleteAll();
      userRepository.deleteAll();
    }
  
    @Test
    public void test_add_follower()
    {
            User user = new User(1, "my bio", "user", "user@email", LocalDateTime.now(), false, Role.SUPERADMIN, User.Status.ONLINE);
            User follower = new User(2, "my bio", "follower", "me@email", LocalDateTime.now(), false, Role.SUPERADMIN, User.Status.ONLINE);

            user = userRepository.save(user);
            follower = userRepository.save(follower);

            Followers newFollower = followersService.addFollower(user, follower);

            assertNotNull(newFollower);
            assertEquals(user.getId(), newFollower.getUser().getId());

    }

    @Test 
    public void test_add_follower_with_null_args()
    {
        
        User user = new User(1, "my bio", "user", "user@email", LocalDateTime.now(), false, Role.SUPERADMIN, User.Status.ONLINE);
        User follower = new User(2, "my bio", "follower", "me@email", LocalDateTime.now(), false, Role.SUPERADMIN, User.Status.ONLINE);

        user = userRepository.save(user);
        follower = userRepository.save(follower);

        Followers followersWithNullFollower = followersService.addFollower(user, null);
        Followers followersWithNullUser = followersService.addFollower(null, null);
        Followers followersWithNullArgs = followersService.addFollower(null, null);
        
        assertNull(followersWithNullFollower);
        assertNull(followersWithNullUser);
        assertNull(followersWithNullArgs);
        
    }

    @Test
    public void test_get_all_followers()
    {
        User user = new User(1, "my bio", "user", "user@email", LocalDateTime.now(), false, Role.SUPERADMIN, User.Status.ONLINE);
        User follower = new User(2, "my bio", "follower", "me@email", LocalDateTime.now(), false, Role.SUPERADMIN, User.Status.ONLINE);
        User follower2 = new User(3, "my bio", "follower2", "follower2@email", LocalDateTime.now(), false, Role.SUPERADMIN, User.Status.ONLINE);    
        
        user =  userRepository.save(user);
        follower = userRepository.save(follower);
        follower2 = userRepository.save(follower2);

       
        Followers followers = new Followers(1,user,follower, Followers.Status.ACCEPTED);
        Followers followers2 = new Followers(2,user, follower2, Followers.Status.ACCEPTED);

        followersRepository.save(followers);
        followersRepository.save(followers2);

        List<User> followersList = followersService.getAllFollowers(user);

        assertNotNull(followersList);
        assertEquals(followersList.size(), 2);
    }

    @Test
    public void test_get_all_followers_with_null_arguments()
    {
        List<User> followersList = followersService.getAllFollowers(null);

        assertNull(followersList);
    }

    @Test
    public void test_get_user_following()
    {
        User user = new User(1, "my bio", "user", "user@email", LocalDateTime.now(), false, Role.SUPERADMIN, User.Status.ONLINE);
        User following = new User(2, "my bio", "follower", "me@email", LocalDateTime.now(), false, Role.SUPERADMIN, User.Status.ONLINE);
        User following2 = new User(3, "my bio", "follower2", "follower2@email", LocalDateTime.now(), false, Role.SUPERADMIN, User.Status.ONLINE);    
        
        user =  userRepository.save(user);
        following = userRepository.save(following);
        following2 = userRepository.save(following2);

       
        Followers followers = new Followers(1,following,user,Status.ACCEPTED);
        Followers followers2 = new Followers(2,following2,user,Status.ACCEPTED);

        followersRepository.save(followers);
        followersRepository.save(followers2);

        List<User> followingList = followersService.getUserFollowing(user);

        assertNotNull(followingList);
        assertEquals(followingList.size(), 2);
    }

   
    @Test
    public void test_user_following_with_null_arguments()
    {
        List<User> followingList = followersService.getUserFollowing(null);

        assertNull(followingList);
    }
    
    @Test
    public void test_remove_follower()
    {
        User user = new User(1, "my bio", "user", "user@email", LocalDateTime.now(), false, Role.SUPERADMIN, User.Status.ONLINE);
        User follower = new User(2, "my bio", "follower", "me@email", LocalDateTime.now(), false, Role.SUPERADMIN, User.Status.ONLINE);

        user = userRepository.save(user);
        follower = userRepository.save(follower);

        Followers followers = new Followers(1,user,follower, Followers.Status.ACCEPTED);

        followersRepository.save(followers);
        
        boolean isDeleted = followersService.removeFollower(user, follower);

        assertTrue(isDeleted);
    }


    @Test
    public void test_remove_follower_with_null_arguments()
    {
        User user = new User(1, "my bio", "user", "user@email", LocalDateTime.now(), false, Role.SUPERADMIN, User.Status.ONLINE);
        User follower = new User(2, "my bio", "follower", "me@email", LocalDateTime.now(), false, Role.SUPERADMIN, User.Status.ONLINE);

        user = userRepository.save(user);
        follower = userRepository.save(follower);
        
        boolean deletedWithNullFollower = followersService.removeFollower(user, null);
        boolean deletedWithNullUser = followersService.removeFollower(null, follower);
        boolean deletedWithNullArguments = followersService.removeFollower(null, null);
        
        assertFalse(deletedWithNullFollower);
        assertFalse(deletedWithNullUser);
        assertFalse(deletedWithNullArguments);
    }

       
    @Test
    public void test_valid_follow_request() throws Exception{
      

      User user = new User(2, "my bio", "me", "me@email", LocalDateTime.now(), false, Role.SUPERADMIN, User.Status.ONLINE);
      user = userRepository.save(user);

      User follower = new User(3, "my bio2", "three", "me@email2", LocalDateTime.now(), false, Role.SUPERADMIN, User.Status.ONLINE);
      follower = userRepository.save(follower);

      System.out.println("this is the user ID: " +  user.getId());
      System.out.println("this is the follower ID: " +  follower.getId());
      assertNotNull(user);
      assertNotNull(follower);
      Map<String, String> requestBody = Map.ofEntries(Map.entry("user", "me"), Map.entry("follower", "three"));

      ResponseEntity<String> response = this.restTemplate.postForEntity("http://localhost:" + port + "/api/followers/add", requestBody, String.class);
      Followers following = followersService.addFollower(user, follower);
      assertEquals("Follower request sent", response.getBody());
      assertNotNull(following);
    }


}
