package com.csci3130.group04.Daltweets.service;

import java.util.List;

import com.csci3130.group04.Daltweets.model.Group;
import com.csci3130.group04.Daltweets.model.User;

public interface GroupService {
    public Group getGroupByName( Group group );
    public boolean isValidToDelete(String username, String groupname);
    public Group deleteGroup ( Group group );
    public List<User> getGroupAdmins(String groupName);
}
