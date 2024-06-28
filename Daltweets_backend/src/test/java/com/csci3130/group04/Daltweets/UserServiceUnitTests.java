package com.csci3130.group04.Daltweets;

import com.csci3130.group04.Daltweets.model.User;
import com.csci3130.group04.Daltweets.repository.UserRepository;
import com.csci3130.group04.Daltweets.service.Implementation.UserServiceImplementation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTests {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImplementation userService = new UserServiceImplementation();

    @Test
    public void testCreateUser() {
        User user = new User(1,"checkbio","Name","mail", LocalDateTime.now(),false, User.Role.SUPERADMIN, User.Status.ONLINE);

        Mockito.when(userRepository.save(user)).thenReturn(user);
        User savedUser = userService.createUser(user);
        assertEquals(user.getBio(), savedUser.getBio());
        assertNotNull(user.getDateCreated());
    }

    @Test
    public void testCreateUserWithNull() {
        assertThrows(Throwable.class,() -> userService.createUser(null));
    }

    @Test
    public void testCreateUserWithoutEnoughInformation() {
        User user = new User(1,"checkbio",null,null,LocalDateTime.now(),false,User.Role.SUPERADMIN, User.Status.ONLINE);
        assertThrows(Throwable.class,() -> userService.createUser(user));
    }

    @Test
    public void testgetUserByName() {
        User user = new User(1,"checkbio","Name","mail", LocalDateTime.now(),false, User.Role.SUPERADMIN, User.Status.ONLINE);
        userService.createUser(user);
        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        User new_user = userService.getUserByName(user.getUsername());

        assertEquals(user,new_user);
    }

    @Test
    public void testgetUserByNameWithoutName() {
        assertThrows(Throwable.class,() -> userService.getUserByName(null));
    }

    @Test
    public void testUpdateUser() {
        User user = new User(1,"checkbio","Name","mail", LocalDateTime.now(),false, User.Role.SUPERADMIN, User.Status.ONLINE);

        User update_user = new User(1,"new_bio","Name","new_mail", LocalDateTime.now(),true, User.Role.SUPERADMIN, User.Status.ONLINE);
        Mockito.when(userRepository.save(user)).thenReturn(user);
        userService.createUser(user);

        Mockito.when(userRepository.save(update_user)).thenReturn(update_user);
        Mockito.when(userRepository.findByUsername(update_user.getUsername())).thenReturn(update_user);
        User updated_user = userService.updateUser(update_user);
        assertEquals(update_user,updated_user);
    }
    @Test
    public void tesUpdateUserWithNull() {
        assertThrows(Throwable.class,() -> userService.updateUser(null));
    }
}
