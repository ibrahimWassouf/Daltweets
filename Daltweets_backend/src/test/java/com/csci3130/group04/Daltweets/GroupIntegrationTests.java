package com.csci3130.group04.Daltweets;

import com.csci3130.group04.Daltweets.model.Group;
import com.csci3130.group04.Daltweets.model.GroupMembers;
import com.csci3130.group04.Daltweets.model.User;
import com.csci3130.group04.Daltweets.repository.GroupMembersRepository;
import com.csci3130.group04.Daltweets.repository.GroupRepository;
import com.csci3130.group04.Daltweets.repository.UserRepository;
import com.csci3130.group04.Daltweets.service.Implementation.GroupServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Map;

@SpringBootTest(classes = DaltweetsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class GroupIntegrationTests {
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    GroupServiceImpl groupService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @AfterEach
    void teardown() {
        groupRepository.deleteAll();
    }

    @Test
    public void testGetGroupByName() {
        Group group = new Group(1,"group1", LocalDateTime.now(),false);

        groupRepository.save(group);

        Group result = groupService.getGroupByName(group);

        assertEquals(group.getName(),result.getName());
    }

    @Test
    public void testGetGroupByNameWithNull() {
        assertThrows(Throwable.class,() -> groupService.getGroupByName(null));
    }

    @Test
    public void testDeleteGroup() {
        Group group = new Group(1,"group1", LocalDateTime.now(),false);

        groupRepository.save(group);

        Group result = groupService.deleteGroup(group);

        assertEquals(group.getName(),result.getName());
    }

    @Test
    public void testDeleteGroupWithNull() {
        assertThrows(Throwable.class,() -> groupService.deleteGroup(null));
    }

    @Test
    public void testDeleteGroupWithNonExistingGroup() {
        Group group = new Group(1,"group1", LocalDateTime.now(),false);

        assertThrows(Throwable.class,() -> groupService.deleteGroup(group));
    }

    @Test
    public void test_controller_delete_group() {
        Group group = new Group(1,"group1", LocalDateTime.now(),false);
        Group saved_group = groupRepository.save(group);

        User user = new User(1,"checkbio","Name","mail", LocalDateTime.now(),false, User.Role.SUPERADMIN, User.Status.ONLINE);
        User saved_user = userRepository.save(user);

        Map<String,String> requestBody = Map.ofEntries(Map.entry("username","Name"),Map.entry("name","group1"));
        ResponseEntity<Group> response = this.restTemplate.postForEntity("http://localhost:" + port + "api/group/delete",requestBody,Group.class);

        String status = "200 OK";
        assertEquals(saved_group.getName(),response.getBody().getName());
        assertEquals(status,response.getStatusCode().toString());
    }

    @Test
    public void test_controller_delete_group_without_admin() {
        Group group = new Group(1,"group1", LocalDateTime.now(),false);
        Group saved_group = groupRepository.save(group);

        User user = new User(1,"checkbio","Name","mail", LocalDateTime.now(),false, User.Role.SUPERADMIN, User.Status.ONLINE);
        User saved_user = userRepository.save(user);

        Map<String,String> requestBody = Map.ofEntries(Map.entry("username","Name"),Map.entry("name","group1"));
        ResponseEntity<Group> response = this.restTemplate.postForEntity("http://localhost:" + port + "api/group/delete",requestBody,Group.class);

        String status = "400 BAD_REQUEST";
        assertNull(response.getBody());
        assertEquals(status,response.getStatusCode().toString());
    }

    @Test
    public void test_controller_delete_group_with_nonExist_group() {
        Group group = new Group(1,"group1", LocalDateTime.now(),false);

        User user = new User(1,"checkbio","Name","mail", LocalDateTime.now(),false, User.Role.SUPERADMIN, User.Status.ONLINE);
        User saved_user = userRepository.save(user);


        Map<String,String> requestBody = Map.ofEntries(Map.entry("username","Name"),Map.entry("name","group1"));
        ResponseEntity<Group> response = this.restTemplate.postForEntity("http://localhost:" + port + "api/group/delete",requestBody,Group.class);

        String status = "400 BAD_REQUEST";
        assertNull(response.getBody());
        assertEquals(status,response.getStatusCode().toString());
    }
}
