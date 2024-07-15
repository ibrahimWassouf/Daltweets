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
import org.springframework.http.HttpStatus;
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
        user =  userRepository.save(user);
        User following = new User(user.getId()+1, "my bio", "follower", "me@email", LocalDateTime.now(), false, Role.SUPERADMIN, User.Status.ONLINE);
        following = userRepository.save(following);
        User following2 = new User(following.getId()+1, "my bio", "follower2", "follower2@email", LocalDateTime.now(), false, Role.SUPERADMIN, User.Status.ONLINE);    
        following2 = userRepository.save(following2);

       
        Followers followers = new Followers(1,following,user,Status.ACCEPTED);
        followers = followersRepository.save(followers);
        Followers followers2 = new Followers(followers.getId()+1,following2,user,Status.ACCEPTED);
        followers2 = followersRepository.save(followers2);

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



    @Test
    public void test_accept_follow_request()
    {
        User user = new User(1, "my bio", "user", "user@email", LocalDateTime.now(), false, Role.SUPERADMIN, User.Status.ONLINE);
        User follower = new User(2, "my bio", "follower", "me@email", LocalDateTime.now(), false, Role.SUPERADMIN, User.Status.ONLINE);

        user = userRepository.save(user);
        follower = userRepository.save(follower);

        Followers followers = new Followers(1,user,follower, Followers.Status.PENDING);

        followers = followersRepository.save(followers);

        Boolean acceptedRequest = followersService.acceptFollowRequest(user,follower);
        followers = followersRepository.findById(followers.getId()).get();
        
        assertTrue(acceptedRequest,"Request should have been accepted and returned true");
        assertEquals(followers.getStatus(), Status.ACCEPTED);
    }

    @Test void test_accept_non_existent_follow_request(){
        User user = new User(1, "my bio", "user", "user@email", LocalDateTime.now(), false, Role.SUPERADMIN, User.Status.ONLINE);
        User follower = new User(2, "my bio", "follower", "me@email", LocalDateTime.now(), false, Role.SUPERADMIN, User.Status.ONLINE);

        user = userRepository.save(user);
        follower = userRepository.save(follower);

        Boolean requestResult = followersService.acceptFollowRequest(user,follower);

        assertFalse(requestResult,"Follow request is non existent so false should be returned");
    }

    @Test void test_accept_follow_request_with_null_args()
    {
        User user = new User(1, "my bio", "user", "user@email", LocalDateTime.now(), false, Role.SUPERADMIN, User.Status.ONLINE);
        User follower = new User(2, "my bio", "follower", "me@email", LocalDateTime.now(), false, Role.SUPERADMIN, User.Status.ONLINE);

        user = userRepository.save(user);
        follower = userRepository.save(follower);

        Followers followers = new Followers(1,user,follower, Followers.Status.PENDING);

        followers = followersRepository.save(followers);

        Boolean requestWithNullUser = followersService.acceptFollowRequest(null,follower);
        Boolean requestWithNullFollower = followersService.acceptFollowRequest(user,null);
        Boolean requestWithNullArgs = followersService.acceptFollowRequest(null,null);
        
        assertFalse(requestWithNullUser,"The user passed to accept follow request was null so the return should be false");
        assertFalse(requestWithNullFollower,"The follower passed to acceptFollowRequest was null so the return should be false");
        assertFalse(requestWithNullArgs,"The arguments passed to accept follow request were null so the return should be false");
    }

    @Test
    public void test_get_follow_requests()
    {
        User user = new User(1, "my bio", "user", "user@email", LocalDateTime.now(), false, Role.SUPERADMIN, User.Status.ONLINE);
        User follower = new User(2, "my bio", "follower", "me@email", LocalDateTime.now(), false, Role.SUPERADMIN, User.Status.ONLINE);

        User follower2 = new User(3, "my bio", "follower2", "follower2@email", LocalDateTime.now(), false, Role.SUPERADMIN, User.Status.ONLINE);    
        
        user =  userRepository.save(user);
        follower = userRepository.save(follower);
        follower2 = userRepository.save(follower2);

       
        Followers followers = new Followers(1,user,follower, Followers.Status.PENDING);
        Followers followers2 = new Followers(2,user, follower2, Followers.Status.PENDING);

        followersRepository.save(followers);
        followersRepository.save(followers2);

        List<Followers> followRequests = followersService.getFollowRequests(user);
        
        assertNotNull(followRequests);
        assertEquals(followRequests.size(), 2,"Wrong Size of followRequests seen");
    }

    @Test
    public void test_get_follow_requests_with_null_args()
    {
        List<Followers> followRequests = followersService.getFollowRequests(null);
        
        assertNull(followRequests,"The User passed to follow requests was null so the return should be false");
    }

    @Test
    public void test_add_follower_with_controller()
    {
            User user = new User(1, "my bio", "user", "user@email", LocalDateTime.now(), false, Role.SUPERADMIN, User.Status.ONLINE);
            User follower = new User(2, "my bio", "follower", "me@email", LocalDateTime.now(), false, Role.SUPERADMIN, User.Status.ONLINE);

            user = userRepository.save(user);
            follower = userRepository.save(follower);

            Map<String,String> requestBody = Map.ofEntries(Map.entry("user",user.getUsername()),Map.entry("follower",follower.getUsername()));
            ResponseEntity<String> response = this.restTemplate.postForEntity("http://localhost:" + port + "/api/followers/add",requestBody,String.class);

            assertNotNull(response);
            assertEquals(HttpStatus.OK,response.getStatusCode());
            assertEquals("Follower request sent", response.getBody());
    } 

    @Test 
    public void test_add_follower_with_non_existent_user_with_controller()
    {  
        User user = new User(1, "my bio", "user", "user@email", LocalDateTime.now(), false, Role.SUPERADMIN, User.Status.ONLINE);
        User follower = new User(2, "my bio", "follower", "me@email", LocalDateTime.now(), false, Role.SUPERADMIN, User.Status.ONLINE);

        user = userRepository.save(user);
        follower = userRepository.save(follower);

        Map<String,String> requestBody = Map.ofEntries(Map.entry("user",""),Map.entry("follower",follower.getUsername()));
        ResponseEntity<String> response = this.restTemplate.postForEntity("http://localhost:" + port + "/api/followers/add",requestBody,String.class);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST,response.getStatusCode());
        assertEquals("Error sending follower request.", response.getBody());
    }

    @Test
    public void test_get_all_followers_with_controller()
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

        ResponseEntity<List> response = this.restTemplate.getForEntity("http://localhost:" + port + "/api/followers/"+user.getUsername()+"/followers",List.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    public void test_get_all_followers_for_non_existent_user_with_controller()
    {
        ResponseEntity<List> response = this.restTemplate.getForEntity("http://localhost:" + port + "/api/followers/user/followers",List.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void test_get_all_followers_for_null_username_with_controller()
    {
        ResponseEntity<List> response = this.restTemplate.getForEntity("http://localhost:" + port + "/api/followers/"+null+"/followers",List.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void test_get_all_following_with_controller()
    {
        User user = new User(1, "my bio", "user", "user@email", LocalDateTime.now(), false, Role.SUPERADMIN, User.Status.ONLINE);
        user =  userRepository.save(user);
        User following = new User(user.getId()+1, "my bio", "follower", "me@email", LocalDateTime.now(), false, Role.SUPERADMIN, User.Status.ONLINE);
        following = userRepository.save(following);
        User following2 = new User(following.getId()+1, "my bio", "follower2", "follower2@email", LocalDateTime.now(), false, Role.SUPERADMIN, User.Status.ONLINE);    
        following2 = userRepository.save(following2);

       
        Followers followers = new Followers(1,following,user,Status.ACCEPTED);
        followers = followersRepository.save(followers);
        Followers followers2 = new Followers(followers.getId()+1,following2,user,Status.ACCEPTED);
        followers2 = followersRepository.save(followers2);


        ResponseEntity<List> response = this.restTemplate.getForEntity("http://localhost:" + port + "/api/followers/"+user.getUsername()+"/following",List.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    public void test_remove_follower_with_controller()
    {
        User user = new User(1, "my bio", "user", "user@email", LocalDateTime.now(), false, Role.SUPERADMIN, User.Status.ONLINE);
        User follower = new User(2, "my bio", "follower", "me@email", LocalDateTime.now(), false, Role.SUPERADMIN, User.Status.ONLINE);

        user = userRepository.save(user);
        follower = userRepository.save(follower);

        Followers followers = new Followers(1,user,follower, Followers.Status.ACCEPTED);

        followersRepository.save(followers);

        Map<String,String> requestBody = Map.ofEntries(Map.entry("user",user.getUsername()),Map.entry("follower",follower.getUsername()));
        ResponseEntity<String> response = this.restTemplate.postForEntity("http://localhost:" + port + "/api/followers/delete",requestBody,String.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals("Follower request deleted", response.getBody());
    }

    @Test
    public void test_remove_follower_for_non_existent_follower_with_controller()
    {
        User user = new User(1, "my bio", "user", "user@email", LocalDateTime.now(), false, Role.SUPERADMIN, User.Status.ONLINE);

        user = userRepository.save(user);

        Map<String,String> requestBody = Map.ofEntries(Map.entry("user",user.getUsername()),Map.entry("follower","name"));
        ResponseEntity<String> response = this.restTemplate.postForEntity("http://localhost:" + port + "/api/followers/delete",requestBody,String.class);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST,response.getStatusCode());
        assertEquals("Error deleting follower request.", response.getBody());
    }
}
