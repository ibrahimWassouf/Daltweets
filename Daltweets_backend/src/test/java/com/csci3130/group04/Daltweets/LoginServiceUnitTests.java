package com.csci3130.group04.Daltweets;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import com.csci3130.group04.Daltweets.model.Login;
import com.csci3130.group04.Daltweets.model.User;
import com.csci3130.group04.Daltweets.model.User.Role;
import com.csci3130.group04.Daltweets.repository.LoginRepository;
import com.csci3130.group04.Daltweets.service.LoginService;
import com.csci3130.group04.Daltweets.service.Implementation.LoginServiceImpl;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class LoginServiceUnitTests {
   @Mock
   LoginRepository loginRepository;

   @InjectMocks
   LoginService loginService = new LoginServiceImpl();


   @Test
   void test_create_login()
   {
    User user = new User(1, "my bio", "me", "me@dal.ca", LocalDateTime.now(), false, Role.SUPERADMIN, null);
    Login login = new Login(1,user.getEmail(),"Password1!","is it me","yes",user);

    when(loginRepository.save(login)).thenReturn(login);
    assertTrue(loginService.createLogin(login));
   }

   @Test
   void test_create_login_with_null_login(){
    Login login = null;
    assertFalse(loginService.createLogin(login));
   }

   @Test
   void test_create_login_with_null_user()
   {
    Login login = new Login(1,"me@me.com","password","is it me","yes",null);
    assertFalse(loginService.createLogin(login));
   }

   @Test 
   void test_create_login_with_invalid_user_name()
   {
    User user = new User(1, "my bio", "me", "me@dal.ca", LocalDateTime.now(), false, Role.SUPERADMIN, null);
    Login login = new Login(1,"invalid.com","password","is it me","yes",user);

    assertFalse(loginService.createLogin(login));
   }

   @Test
   void test_create_login_with_invalid_password()
   {
    User user = new User(1, "my bio", "me", "me@dal.ca", LocalDateTime.now(), false, Role.SUPERADMIN, null);
    Login login = new Login(1,user.getEmail(),"pass","is it me","yes",user);

    assertFalse(loginService.createLogin(login));
   }
}
