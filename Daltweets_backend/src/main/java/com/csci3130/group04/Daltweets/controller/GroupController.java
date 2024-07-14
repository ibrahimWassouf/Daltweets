package com.csci3130.group04.Daltweets.controller;

import com.csci3130.group04.Daltweets.model.Group;
import com.csci3130.group04.Daltweets.service.Implementation.GroupServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
    @GetMapping("/{username}/groups")
    ResponseEntity<List<Group>> getGroups(@PathVariable("username") String username) {
        List<Group> Groups = new ArrayList<>();
        return new ResponseEntity<>(Groups,HttpStatus.OK);
    }
}
