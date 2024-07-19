package com.csci3130.group04.Daltweets.controller;

import com.csci3130.group04.Daltweets.model.Group;
import com.csci3130.group04.Daltweets.model.GroupMembers;
import com.csci3130.group04.Daltweets.model.User;
import com.csci3130.group04.Daltweets.service.Implementation.GroupServiceImpl;
import com.csci3130.group04.Daltweets.service.Implementation.UserServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/group")
public class GroupController {
    @Autowired
    GroupServiceImpl groupService;
    @Autowired
    UserServiceImplementation userService;

    @PostMapping("/delete")
    ResponseEntity<Group> deleteGroup(@RequestBody Map<String,String> requestBody) {
        String user_name = requestBody.get("username");
        String group_name = requestBody.get("name");
        Group group = null;
        if ( groupService.isValidToDelete(user_name,group_name) ) {
            group = new Group();
            group.setName(group_name);
            group = groupService.deleteGroup(group);
        } else {
            return new ResponseEntity<>(group,HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(group, HttpStatus.OK);
    }

    @PostMapping("/create")
    ResponseEntity<Group> createGroup(@RequestBody Group group) {
        Group groupCreated = groupService.createGroup(group);
        if (groupCreated == null) {
            return new ResponseEntity<>(group, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(group, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    ResponseEntity<List<Group>> getAllGroups(){
        List<Group> groups = groupService.getAllGroups();
        return new ResponseEntity<>(groups,HttpStatus.OK);
    }

    @GetMapping("/{username}/groups")
    ResponseEntity<List<Group>> getGroups(@PathVariable("username") String username) {
        if ( username == null || username.isBlank() ) {
            return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        }
        List<Group> groups = groupService.getGroupsByUser(username);
        return new ResponseEntity<>(groups,HttpStatus.OK);
    }

    @GetMapping("/{groupName}/members")
    ResponseEntity<List<User>> getGroupMembers(@PathVariable("groupName") String groupName)
    {
        if (groupName == null || groupName.isBlank()) return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        List<User> groupMembers = groupService.getGroupMembers(groupName);  
        if (groupMembers == null) return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(groupMembers,HttpStatus.OK);
    }
    

    @GetMapping("/{groupName}/admins")
    ResponseEntity<List<User>> getGroupAdmins(@PathVariable("groupName") String groupName)
    {
        if (groupName == null || groupName.isBlank()) return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        List<User> groupAdmins = groupService.getGroupAdmins(groupName);  
        if (groupAdmins == null) return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(groupAdmins,HttpStatus.OK);
    }
    @PostMapping("/deleteuser")
    ResponseEntity<GroupMembers> deleteUser(@RequestBody Map<String,String> requestBody) {
        String adminname = requestBody.get("adminname");
        String username = requestBody.get("username");
        String groupname = requestBody.get("name");

        if (!userService.isValidName(adminname) || !userService.isValidName(username) || !userService.isValidName(groupname) ) {
            return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        }

        if ( !groupService.isValidToDelete(adminname,groupname) ) {
            return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        }

        GroupMembers groupMembers = groupService.deleteUser(username,groupname);
        if ( groupMembers == null ) {
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(groupMembers,HttpStatus.OK);
    }
}
