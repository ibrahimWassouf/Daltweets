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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
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
    GroupMembersRepository groupMembersRepository;
    @Autowired
    TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @AfterEach
    void teardown() {
        groupMembersRepository.deleteAll();
        groupRepository.deleteAll();
        userRepository.deleteAll();
        
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

        GroupMembers groupMembers = new GroupMembers(1,saved_group,saved_user,true);
        GroupMembers saved_groupMembers = groupMembersRepository.save(groupMembers);

        Map<String,String> requestBody = Map.ofEntries(Map.entry("username","Name"),Map.entry("name","group1"));
        ResponseEntity<Group> response = this.restTemplate.postForEntity("http://localhost:" + port + "/api/group/delete",requestBody,Group.class);
        
        String status = "200 OK";
        boolean deleted = true;
        assertEquals(deleted,response.getBody().getIsDeleted());
        assertEquals(saved_group.getName(),response.getBody().getName());
        assertEquals(status,response.getStatusCode().toString());
    }

    @Test
    public void test_controller_delete_group_without_admin() {
        Group group = new Group(1,"group1", LocalDateTime.now(),false);
        Group saved_group = groupRepository.save(group);

        User user = new User(1,"checkbio","Name","mail", LocalDateTime.now(),false, User.Role.SUPERADMIN, User.Status.ONLINE);
        User saved_user = userRepository.save(user);

        GroupMembers groupMembers = new GroupMembers(1,saved_group,saved_user,false);
        GroupMembers saved_groupMembers = groupMembersRepository.save(groupMembers);

        Map<String,String> requestBody = Map.ofEntries(Map.entry("username","Name"),Map.entry("name","group1"));
        ResponseEntity<Group> response = this.restTemplate.postForEntity("http://localhost:" + port + "/api/group/delete",requestBody,Group.class);

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
        ResponseEntity<Group> response = this.restTemplate.postForEntity("http://localhost:" + port + "/api/group/delete",requestBody,Group.class);

        String status = "400 BAD_REQUEST";
        assertNull(response.getBody());
        assertEquals(status,response.getStatusCode().toString());
    }

    @Test
    public void test_get_group_members(){
        Group group = new Group(1,"group1", LocalDateTime.now(),false);
        group = groupRepository.save(group);

        User user = new User(1,"checkbio","Name","mail", LocalDateTime.now(),false, User.Role.SUPERADMIN, User.Status.ONLINE);
        user = userRepository.save(user);

        GroupMembers groupMembers = new GroupMembers(1,group, user,true);
        groupMembers = groupMembersRepository.save(groupMembers);

        ResponseEntity<List> response = this.restTemplate.getForEntity("http://localhost:" + port + "/api/group/" + group.getName()+"/members",List.class);
       
        assertNotNull(response);
        assertEquals(1, response.getBody().size(),"Size of the group members list is incorrect");
    }

    @Test 
    public void test_get_group_members_with_no_group_name()
    {
        ResponseEntity<List> response = this.restTemplate.getForEntity("http://localhost:" + port + "/api/group/ /members",List.class);
        assertNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void test_get_group_members_with_non_existent_group(){
        ResponseEntity<List> response = this.restTemplate.getForEntity("http://localhost:" + port + "/api/group/group1/members",List.class);
        assertNull(response.getBody());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }


    @Test
    public void test_get_group_admins(){
        Group group = new Group(1,"group1", LocalDateTime.now(),false);
        group = groupRepository.save(group);

        User user = new User(1,"checkbio","Name","mail", LocalDateTime.now(),false, User.Role.SUPERADMIN, User.Status.ONLINE);
        user = userRepository.save(user);

        GroupMembers groupMembers = new GroupMembers(1,group, user,true);
        groupMembers = groupMembersRepository.save(groupMembers);

        ResponseEntity<List> response = this.restTemplate.getForEntity("http://localhost:" + port + "/api/group/" + group.getName()+"/admins",List.class);
       
        assertNotNull(response);
        assertEquals(1, response.getBody().size(),"Size of the group members list is incorrect");
    }


    @Test
    public void test_get_group_admins_with_no_admins(){
        Group group = new Group(1,"group1", LocalDateTime.now(),false);
        group = groupRepository.save(group);

        User user = new User(1,"checkbio","Name","mail", LocalDateTime.now(),false, User.Role.SUPERADMIN, User.Status.ONLINE);
        user = userRepository.save(user);

        GroupMembers groupMembers = new GroupMembers(1,group, user,false);
        groupMembers = groupMembersRepository.save(groupMembers);

        ResponseEntity<List> response = this.restTemplate.getForEntity("http://localhost:" + port + "/api/group/" + group.getName()+"/admins",List.class);

        assertNotNull(response);
        assertEquals(0, response.getBody().size(),"Size of the group members list should be empty");
    }


    @Test 
    public void test_get_group_admins_with_no_group_name()
    {
        ResponseEntity<List> response = this.restTemplate.getForEntity("http://localhost:" + port + "/api/group/ /admins",List.class);
        assertNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void test_get_group_admins_with_non_existent_group(){
        ResponseEntity<List> response = this.restTemplate.getForEntity("http://localhost:" + port + "/api/group/group1/admins",List.class);
        assertNull(response.getBody());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
