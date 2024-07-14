package com.csci3130.group04.Daltweets.controller;

import com.csci3130.group04.Daltweets.model.Group;
import com.csci3130.group04.Daltweets.model.User;
import com.csci3130.group04.Daltweets.service.Implementation.GroupServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/{groupName}/admins")
    ResponseEntity<List<User>> getGroupAdmins(@PathVariable("groupName") String groupName)
    {
        if (groupName == null || groupName.isBlank()) return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        List<User> groupAdmins = groupService.getGroupAdmins(groupName);  
        if (groupAdmins == null) return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(groupAdmins,HttpStatus.OK);
    }
}
