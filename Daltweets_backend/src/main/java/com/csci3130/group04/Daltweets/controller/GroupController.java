package com.csci3130.group04.Daltweets.controller;

import com.csci3130.group04.Daltweets.model.Group;
import com.csci3130.group04.Daltweets.model.User;
import com.csci3130.group04.Daltweets.service.Implementation.GroupServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/group")
public class GroupController {
    @Autowired
    GroupServiceImpl groupService;

    @PostMapping("/delete")
    ResponseEntity<Group> deleteGroup(@RequestBody Map<String,String> requestBody) {
        Group group = new Group();
        return new ResponseEntity<>(group, HttpStatus.OK);
    }
}
