package com.csci3130.group04.Daltweets;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


import com.csci3130.group04.Daltweets.model.Followers;
import com.csci3130.group04.Daltweets.model.User;
import com.csci3130.group04.Daltweets.model.User.Role;
import com.csci3130.group04.Daltweets.repository.FollowersRepository;
import com.csci3130.group04.Daltweets.repository.UserRepository;
import com.csci3130.group04.Daltweets.service.FollowersService;

import jakarta.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;


@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class FollowersIntegrationTests {

    @Autowired 
    private FollowersService followersService;

    @Autowired
    private FollowersRepository followersRepository;

    @Autowired
    private UserRepository userRepository;

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

        assertNotNull(followers);
        assertEquals(followersList.size(), 2);
    }

    @Test
    public void test_get_all_followers_with_null_arguments()
    {
        List<User> followersList = followersService.getAllFollowers(null);

        assertNull(followersList);
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
}
