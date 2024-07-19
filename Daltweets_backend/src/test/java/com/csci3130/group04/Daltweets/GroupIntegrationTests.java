package com.csci3130.group04.Daltweets;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

import com.csci3130.group04.Daltweets.model.Group;
import com.csci3130.group04.Daltweets.model.GroupMembers;
import com.csci3130.group04.Daltweets.model.User;
import com.csci3130.group04.Daltweets.repository.GroupMembersRepository;
import com.csci3130.group04.Daltweets.repository.GroupRepository;
import com.csci3130.group04.Daltweets.repository.UserRepository;
import com.csci3130.group04.Daltweets.service.Implementation.GroupServiceImpl;

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
        Group group = new Group(1, "group1", LocalDateTime.now(), false, "");

        groupRepository.save(group);

        Group result = groupService.getGroupByName(group);

        assertEquals(group.getName(), result.getName());
    }

    @Test
    public void testGetGroupByNameWithNull() {
        assertThrows(Throwable.class, () -> groupService.getGroupByName(null));
    }

    @Test
    public void testDeleteGroup() {
        Group group = new Group(1, "group1", LocalDateTime.now(), false, "");

        groupRepository.save(group);

        Group result = groupService.deleteGroup(group);

        assertEquals(group.getName(), result.getName());
    }

    @Test
    public void testDeleteGroupWithNull() {
        assertThrows(Throwable.class, () -> groupService.deleteGroup(null));
    }

    @Test
    public void testDeleteGroupWithNonExistingGroup() {
        Group group = new Group(1, "group1", LocalDateTime.now(), false, "");

        assertThrows(Throwable.class, () -> groupService.deleteGroup(group));
    }

    @Test
    public void testCreatePublicGroup() {
        Group group = new Group(2, "group2", LocalDateTime.now(), true, "");
        groupRepository.save(group);
        Group created = groupService.createGroup(group);
        assertTrue(group.getIsPublic());
        assertEquals(group.getName(), created.getName());
    }

    @Test
    public void testCreatePrivateGroup() {
        Group group = new Group(2, "group2", LocalDateTime.now(), false, "");
        groupRepository.save(group);
        Group created = groupService.createGroup(group);
        assertFalse(group.getIsPublic());
        assertEquals(group.getName(), created.getName());
    }

    @Test
    public void testCreateGroupWithoutName() {
        Group group = new Group(2, null, LocalDateTime.now(), false, "");
        Group createdGroup = groupService.createGroup(group);
        assertNull(createdGroup);
    }

    @Test
    public void testCreateGroupWithoutTimeCreated() {
        Group group = new Group(2, "group2", null, false, "");
        Group created = groupService.createGroup(group);
        assertNotNull(created.getDateCreated());
    }

    @Test
    public void testCreateGroupWithNull(){
        Group createdGroup = groupService.createGroup(null);
        assertNull(createdGroup);
    }

    @Test
    public void testAddUserToGroup(){
        Group group = new Group(1, "group1", LocalDateTime.now(), true, "");
        Group saved_group = groupRepository.save(group);

        User user = new User(1, "checkbio", "Name", "mail", LocalDateTime.now(), false, User.Role.SUPERADMIN, User.Status.ONLINE);
        User saved_user = userRepository.save(user);


        GroupMembers addedGroupMember = groupService.addUser("Name", "group1", false);

        assertEquals(saved_group.getId(), addedGroupMember.getGroup().getId());
        assertEquals(saved_user.getId(), addedGroupMember.getUser().getId());
        assertFalse(addedGroupMember.isAdmin());
    }

    @Test
    public void testAddGroupAdmin(){
        Group group = new Group(1, "group1", LocalDateTime.now(), true, "");
        Group saved_group = groupRepository.save(group);

        User admin = new User(1, "checkbio", "Name", "mail", LocalDateTime.now(), false, User.Role.SUPERADMIN, User.Status.ONLINE);
        User saved_admin = userRepository.save(admin);

        GroupMembers addedGroupMember = groupService.addUser("Name", "group1", true);

        assertEquals(saved_group.getId(), addedGroupMember.getGroup().getId());
        assertEquals(saved_admin.getId(), addedGroupMember.getUser().getId());
        assertTrue(addedGroupMember.isAdmin());
    }

    @Test
    public void testAddUserAlreadyInGroup(){
        Group group = new Group(1, "group1", LocalDateTime.now(), true, "");
        groupRepository.save(group);

        User user = new User(1, "checkbio", "Name", "mail", LocalDateTime.now(), false, User.Role.SUPERADMIN, User.Status.ONLINE);
        userRepository.save(user);

        groupService.addUser("Name", "group1", false);
        GroupMembers alreadyAddedMember = groupService.addUser("Name", "group1", false);

        assertNull(alreadyAddedMember);
    }

    @Test
    public void testAddDeletedUserAccountToGroup(){
        Group group = new Group(1, "group1", LocalDateTime.now(), true, "");
        groupRepository.save(group);

        User user = new User(1, "checkbio", "Name", "mail", LocalDateTime.now(), true, User.Role.SUPERADMIN, User.Status.ONLINE);
        userRepository.save(user);

        IllegalArgumentException exception = (IllegalArgumentException) assertThrows(Throwable.class, () -> groupService.addUser("Name", "group1", false));
        String expectedMessage = "User was not found";
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void testAddUserToDeletedGroup(){
        Group group = new Group(1, "group1", LocalDateTime.now(), true, "");
        group.setIsDeleted(true);
        groupRepository.save(group);

        User user = new User(1, "checkbio", "Name", "mail", LocalDateTime.now(), false, User.Role.SUPERADMIN, User.Status.ONLINE);
        userRepository.save(user);

        IllegalArgumentException exception = (IllegalArgumentException) assertThrows(Throwable.class, () -> groupService.addUser("Name", "group1", false));
        String expectedMessage = "Cannot add user to deleted group";
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void testAddUserToGroupWithNonexistentUser(){
        Group group = new Group(1, "group1", LocalDateTime.now(), true, "");
        groupRepository.save(group);
        
        IllegalArgumentException exception = (IllegalArgumentException) assertThrows(Throwable.class, () -> groupService.addUser("Name", "group1", false));
        String expectedMessage = "User was not found";
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void testAddUserToGroupWithNonexistentGroup(){
        User user = new User(1, "checkbio", "Name", "mail", LocalDateTime.now(), false, User.Role.SUPERADMIN, User.Status.ONLINE);
        userRepository.save(user);

        IllegalArgumentException exception = (IllegalArgumentException) assertThrows(Throwable.class, () -> groupService.addUser("Name", "group1", false));
        String expectedMessage = "Group was not found";
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void testAddUserToGroupWithNullUser(){
        IllegalArgumentException exception = (IllegalArgumentException) assertThrows(Throwable.class, () -> groupService.addUser(null, "group1", false));
        String expectedMessage = "User is null";
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void testAddUserToGroupWithNullGroup(){
        IllegalArgumentException exception = (IllegalArgumentException) assertThrows(Throwable.class, () -> groupService.addUser("Name", null, false));
        String expectedMessage = "Group is null";
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void test_controller_delete_group() {
        Group group = new Group(1, "group1", LocalDateTime.now(), false, "");
        Group saved_group = groupRepository.save(group);

        User user = new User(1, "checkbio", "Name", "mail", LocalDateTime.now(), false, User.Role.SUPERADMIN, User.Status.ONLINE);
        User saved_user = userRepository.save(user);

        GroupMembers groupMembers = new GroupMembers(1, saved_group, saved_user, true);
        GroupMembers saved_groupMembers = groupMembersRepository.save(groupMembers);

        Map<String, String> requestBody = Map.ofEntries(Map.entry("username", "Name"), Map.entry("name", "group1"));
        ResponseEntity<Group> response = this.restTemplate.postForEntity("http://localhost:" + port + "/api/group/delete", requestBody, Group.class);

        String status = "200 OK";
        boolean deleted = true;
        assertEquals(deleted, response.getBody().getIsDeleted());
        assertEquals(saved_group.getName(), response.getBody().getName());
        assertEquals(status, response.getStatusCode().toString());
    }

    @Test
    public void test_controller_delete_group_without_admin() {
        Group group = new Group(1, "group1", LocalDateTime.now(), false, "");
        Group saved_group = groupRepository.save(group);

        User user = new User(1, "checkbio", "Name", "mail", LocalDateTime.now(), false, User.Role.SUPERADMIN, User.Status.ONLINE);
        User saved_user = userRepository.save(user);

        GroupMembers groupMembers = new GroupMembers(1, saved_group, saved_user, false);
        GroupMembers saved_groupMembers = groupMembersRepository.save(groupMembers);

        Map<String, String> requestBody = Map.ofEntries(Map.entry("username", "Name"), Map.entry("name", "group1"));
        ResponseEntity<Group> response = this.restTemplate.postForEntity("http://localhost:" + port + "/api/group/delete", requestBody, Group.class);

        String status = "400 BAD_REQUEST";
        assertNull(response.getBody());
        assertEquals(status, response.getStatusCode().toString());
    }

    @Test
    public void test_controller_delete_group_with_nonExist_group() {
        Group group = new Group(1, "group1", LocalDateTime.now(), false, "");

        User user = new User(1, "checkbio", "Name", "mail", LocalDateTime.now(), false, User.Role.SUPERADMIN, User.Status.ONLINE);
        User saved_user = userRepository.save(user);

        Map<String, String> requestBody = Map.ofEntries(Map.entry("username", "Name"), Map.entry("name", "group1"));
        ResponseEntity<Group> response = this.restTemplate.postForEntity("http://localhost:" + port + "/api/group/delete", requestBody, Group.class);

        String status = "400 BAD_REQUEST";
        assertNull(response.getBody());
        assertEquals(status, response.getStatusCode().toString());
    }

    @Test
    public void test_getGroupsByUser_with_NULL() {
        assertThrows(Throwable.class, () -> groupService.getGroupsByUser(null));
    }

    @Test
    public void test_get_groups() {
        Group group = new Group(1, "group1", LocalDateTime.now(), false, "");
        Group saved_group = groupRepository.save(group);

        User user = new User(1, "checkbio", "Name", "mail", LocalDateTime.now(), false, User.Role.SUPERADMIN, User.Status.ONLINE);
        User saved_user = userRepository.save(user);

        GroupMembers groupMembers = new GroupMembers(1, saved_group, saved_user, false);
        GroupMembers saved_groupMembers = groupMembersRepository.save(groupMembers);

        ResponseEntity<List> response = this.restTemplate.getForEntity("http://localhost:" + port + "/api/group/" + saved_user.getUsername() + "/groups", List.class);

        assertNotNull(response);
        assertEquals(1, response.getBody().size());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void test_get_group_members_with_no_user() {
        User user = new User(1, "checkbio", " ", "mail", LocalDateTime.now(), false, User.Role.SUPERADMIN, User.Status.ONLINE);
        ResponseEntity<List> response = this.restTemplate.getForEntity("http://localhost:" + port + "/api/group/" + user.getUsername() + "/groups", List.class);


        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void test_get_group_members() {
        Group group = new Group(1, "group1", LocalDateTime.now(), false, "");
        group = groupRepository.save(group);

        User user = new User(1, "checkbio", "Name", "mail", LocalDateTime.now(), false, User.Role.SUPERADMIN, User.Status.ONLINE);
        user = userRepository.save(user);

        GroupMembers groupMembers = new GroupMembers(1, group, user, true);
        groupMembers = groupMembersRepository.save(groupMembers);

        ResponseEntity<List> response = this.restTemplate.getForEntity("http://localhost:" + port + "/api/group/" + group.getName() + "/members", List.class);

        assertNotNull(response);
        assertEquals(1, response.getBody().size(), "Size of the group members list is incorrect");
    }

    @Test
    public void test_get_group_members_with_no_group_name() {
        ResponseEntity<List> response = this.restTemplate.getForEntity("http://localhost:" + port + "/api/group/ /members", List.class);
        assertNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void test_get_group_members_with_non_existent_group() {
        ResponseEntity<List> response = this.restTemplate.getForEntity("http://localhost:" + port + "/api/group/group1/members", List.class);
        assertNull(response.getBody());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }


    @Test
    public void test_get_group_admins() {
        Group group = new Group(1, "group1", LocalDateTime.now(), false, "");
        group = groupRepository.save(group);

        User user = new User(1, "checkbio", "Name", "mail", LocalDateTime.now(), false, User.Role.SUPERADMIN, User.Status.ONLINE);
        user = userRepository.save(user);

        GroupMembers groupMembers = new GroupMembers(1, group, user, true);
        groupMembers = groupMembersRepository.save(groupMembers);

        ResponseEntity<List> response = this.restTemplate.getForEntity("http://localhost:" + port + "/api/group/" + group.getName() + "/admins", List.class);

        assertNotNull(response);
        assertEquals(1, response.getBody().size(), "Size of the group members list is incorrect");
    }


    @Test
    public void test_get_group_admins_with_no_admins() {
        Group group = new Group(1, "group1", LocalDateTime.now(), false, "");
        group = groupRepository.save(group);

        User user = new User(1, "checkbio", "Name", "mail", LocalDateTime.now(), false, User.Role.SUPERADMIN, User.Status.ONLINE);
        user = userRepository.save(user);

        GroupMembers groupMembers = new GroupMembers(1, group, user, false);
        groupMembers = groupMembersRepository.save(groupMembers);

        ResponseEntity<List> response = this.restTemplate.getForEntity("http://localhost:" + port + "/api/group/" + group.getName() + "/admins", List.class);

        assertNotNull(response);
        assertEquals(0, response.getBody().size(), "Size of the group members list should be empty");
    }


    @Test
    public void test_get_group_admins_with_no_group_name() {
        ResponseEntity<List> response = this.restTemplate.getForEntity("http://localhost:" + port + "/api/group/ /admins", List.class);
        assertNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void test_get_group_admins_with_non_existent_group() {
        ResponseEntity<List> response = this.restTemplate.getForEntity("http://localhost:" + port + "/api/group/group1/admins", List.class);
        assertNull(response.getBody());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void test_controller_add_groupmember_admin(){
        Group group = new Group(1, "group1", LocalDateTime.now(), false, "");
        Group saved_group = groupRepository.save(group);

        User admin = new User(1, "checkbio", "Name", "mail", LocalDateTime.now(), false, User.Role.SUPERADMIN, User.Status.ONLINE);
        User saved_admin = userRepository.save(admin);

        Map<String, String> requestBody = Map.ofEntries(Map.entry("userName", "Name"), Map.entry("groupName", "group1"), Map.entry("isAdmin", "true"));
        ResponseEntity<GroupMembers> response = this.restTemplate.postForEntity("http://localhost:" + port + "/api/group/adduser", requestBody, GroupMembers.class);

        assertNotNull(response.getBody());
        assertEquals(saved_admin.getId(), response.getBody().getUser().getId());
        assertTrue(response.getBody().isAdmin());
        assertEquals(saved_group.getId(), response.getBody().getGroup().getId());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void test_controller_add_groupmember_user(){
        Group group = new Group(1, "group1", LocalDateTime.now(), false, "");
        Group saved_group = groupRepository.save(group);

        User user = new User(1, "checkbio", "Name", "mail", LocalDateTime.now(), false, User.Role.SUPERADMIN, User.Status.ONLINE);
        User saved_user = userRepository.save(user);

        Map<String, String> requestBody = Map.ofEntries(Map.entry("userName", "Name"), Map.entry("groupName", "group1"), Map.entry("isAdmin", "false"));
        ResponseEntity<GroupMembers> response = this.restTemplate.postForEntity("http://localhost:" + port + "/api/group/adduser", requestBody, GroupMembers.class);

        assertNotNull(response);
        assertEquals(saved_user.getId(), response.getBody().getUser().getId());
        assertFalse(response.getBody().isAdmin());
        assertEquals(saved_group.getId(), response.getBody().getGroup().getId());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void test_controller_add_groupmember_with_null_user(){
        Map<String, String> requestBody = Map.ofEntries(Map.entry("groupName", "group1"));
        ResponseEntity<GroupMembers> response = this.restTemplate.postForEntity("http://localhost:" + port + "/api/group/adduser", requestBody, GroupMembers.class);

        assertNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void test_controller_add_groupmember_with_null_group(){
        Map<String, String> requestBody = Map.ofEntries(Map.entry("userName", "Name"));
        ResponseEntity<GroupMembers> response = this.restTemplate.postForEntity("http://localhost:" + port + "/api/group/adduser", requestBody, GroupMembers.class);

        assertNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void test_controller_add_groupmember_with_existing_member()
    {
        Group group = new Group(1, "group1", LocalDateTime.now(), false, "");
        group = groupRepository.save(group);

        User user = new User(1, "checkbio", "Name", "mail", LocalDateTime.now(), false, User.Role.SUPERADMIN, User.Status.ONLINE);
        user = userRepository.save(user);

        GroupMembers groupMember = new GroupMembers(1,group,user,false);
        groupMember = groupMembersRepository.save(groupMember);

        Map<String, String> requestBody = Map.ofEntries(Map.entry("userName", "Name"), Map.entry("groupName", "group1"), Map.entry("isAdmin", "false"));
        ResponseEntity<GroupMembers> response = this.restTemplate.postForEntity("http://localhost:" + port + "/api/group/adduser", requestBody, GroupMembers.class);

        assertNotNull(response);
        assertNull(response.getBody());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void test_delete_user() {
        Group group = new Group(1, "group1", LocalDateTime.now(), false, "");
        Group saved_group = groupRepository.save(group);

        User admin = new User(1, "checkbio", "Name", "mail", LocalDateTime.now(), false, User.Role.SUPERADMIN, User.Status.ONLINE);
        User saved_admin = userRepository.save(admin);
        User user = new User(2, "checkbio2", "Name2", "mail2", LocalDateTime.now(), false, User.Role.SUPERADMIN, User.Status.ONLINE);
        User saved_user = userRepository.save(user);

        GroupMembers groupadmin = new GroupMembers(1, saved_group, saved_admin, true);
        GroupMembers saved_groupadmin = groupMembersRepository.save(groupadmin);
        GroupMembers groupMembers = new GroupMembers(2, saved_group, saved_user, true);
        GroupMembers saved_groupMembers = groupMembersRepository.save(groupMembers);

        Map<String, String> requestBody = Map.ofEntries(Map.entry("adminname", "Name"), Map.entry("username", "Name2"), Map.entry("name", "group1"));
        ResponseEntity<GroupMembers> response = this.restTemplate.postForEntity("http://localhost:" + port + "/api/group/deleteuser", requestBody, GroupMembers.class);

        GroupMembers deleted = groupMembersRepository.findGroupMembersByUserAndGroup(saved_user.getUsername(), saved_group.getName());

        assertNull(deleted);
        assertNotNull(response);
        assertEquals(saved_groupMembers.getUser().getUsername(), response.getBody().getUser().getUsername());
        assertEquals(saved_groupMembers.getGroup().getName(), response.getBody().getGroup().getName());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void test_delete_user_without_admin() {
        Group group = new Group(1, "group1", LocalDateTime.now(), false, "");
        Group saved_group = groupRepository.save(group);

        User admin = new User(1, "checkbio", "Name", "mail", LocalDateTime.now(), false, User.Role.SUPERADMIN, User.Status.ONLINE);
        User saved_admin = userRepository.save(admin);
        User user = new User(2, "checkbio2", "Name2", "mail2", LocalDateTime.now(), false, User.Role.SUPERADMIN, User.Status.ONLINE);
        User saved_user = userRepository.save(user);

        GroupMembers groupadmin = new GroupMembers(1, saved_group, saved_admin, false);
        GroupMembers saved_groupadmin = groupMembersRepository.save(groupadmin);
        GroupMembers groupMembers = new GroupMembers(2, saved_group, saved_user, true);
        GroupMembers saved_groupMembers = groupMembersRepository.save(groupMembers);

        Map<String, String> requestBody = Map.ofEntries(Map.entry("adminname", "Name"), Map.entry("username", "Name2"), Map.entry("name", "group1"));
        ResponseEntity<GroupMembers> response = this.restTemplate.postForEntity("http://localhost:" + port + "/api/group/deleteuser", requestBody, GroupMembers.class);

        GroupMembers deleted = groupMembersRepository.findGroupMembersByUserAndGroup(saved_user.getUsername(), saved_group.getName());

        assertNotNull(deleted);
        assertNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void test_delete_user_with_nonexist_groupmember() {
        Group group = new Group(1, "group1", LocalDateTime.now(), false, "");
        Group saved_group = groupRepository.save(group);

        User admin = new User(1, "checkbio", "Name", "mail", LocalDateTime.now(), false, User.Role.SUPERADMIN, User.Status.ONLINE);
        User saved_admin = userRepository.save(admin);
        User user = new User(2, "checkbio2", "Name2", "mail2", LocalDateTime.now(), false, User.Role.SUPERADMIN, User.Status.ONLINE);
        User saved_user = userRepository.save(user);

        GroupMembers groupadmin = new GroupMembers(1, saved_group, saved_admin, true);
        GroupMembers saved_groupadmin = groupMembersRepository.save(groupadmin);
        GroupMembers groupMembers = new GroupMembers(2, saved_group, saved_user, true);

        Map<String, String> requestBody = Map.ofEntries(Map.entry("adminname", "Name"), Map.entry("username", "Name2"), Map.entry("name", "group1"));
        ResponseEntity<GroupMembers> response = this.restTemplate.postForEntity("http://localhost:" + port + "/api/group/deleteuser", requestBody, GroupMembers.class);

        GroupMembers deleted = groupMembersRepository.findGroupMembersByUserAndGroup(saved_user.getUsername(), saved_group.getName());

        assertNull(deleted);
        assertNull(response.getBody());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void test_delete_user_with_invalid_name() {
        Group group = new Group(1, "group1", LocalDateTime.now(), false, "");
        Group saved_group = groupRepository.save(group);

        User admin = new User(1, "checkbio", "Name", "mail", LocalDateTime.now(), false, User.Role.SUPERADMIN, User.Status.ONLINE);
        User saved_admin = userRepository.save(admin);
        User user = new User(2, "checkbio2", " ", "mail2", LocalDateTime.now(), false, User.Role.SUPERADMIN, User.Status.ONLINE);
        User saved_user = userRepository.save(user);

        GroupMembers groupadmin = new GroupMembers(1, saved_group, saved_admin, true);
        GroupMembers saved_groupadmin = groupMembersRepository.save(groupadmin);
        GroupMembers groupMembers = new GroupMembers(2, saved_group, saved_user, true);
        GroupMembers saved_groupMembers = groupMembersRepository.save(groupMembers);

        Map<String, String> requestBody = Map.ofEntries(Map.entry("adminname", "Name"), Map.entry("username", " "), Map.entry("name", "group1"));
        ResponseEntity<GroupMembers> response = this.restTemplate.postForEntity("http://localhost:" + port + "/api/group/deleteuser", requestBody, GroupMembers.class);

        assertNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void test_controller_create_public_group() {
        Group group = new Group(2, "group2", LocalDateTime.now(), true, "");
        Group savedGroup = groupRepository.save(group);

        User user = new User(2, "checkbio", "Name", "mail", LocalDateTime.now(), false, User.Role.SUPERADMIN, User.Status.ONLINE);
        User savedUser = userRepository.save(user);

        GroupMembers groupMembers = new GroupMembers(2, savedGroup, savedUser, true);

        Map<String, String> requestBody = Map.ofEntries(Map.entry("username", "Name"), Map.entry("name", "group2"));
        ResponseEntity<Group> response = this.restTemplate.postForEntity("http://localhost:" + port + "/api/group/create", requestBody, Group.class);

        assertEquals(savedGroup.getName(), response.getBody().getName());
        assertEquals("201 CREATED", response.getStatusCode().toString());
    }

    @Test
    public void test_controller_create_private_group() {
        Group group = new Group(2, "group2", LocalDateTime.now(), false, "");
        Group savedGroup = groupRepository.save(group);

        User user = new User(2, "checkbio", "Name", "mail", LocalDateTime.now(), false, User.Role.SUPERADMIN, User.Status.ONLINE);
        User savedUser = userRepository.save(user);

        GroupMembers groupMembers = new GroupMembers(2, savedGroup, savedUser, true);

        Map<String, String> requestBody = Map.ofEntries(Map.entry("username", "Name"), Map.entry("name", "group2"));
        ResponseEntity<Group> response = this.restTemplate.postForEntity("http://localhost:" + port + "/api/group/create", requestBody, Group.class);

        assertEquals(savedGroup.getName(), response.getBody().getName());
        assertEquals("201 CREATED", response.getStatusCode().toString());
    }
}
