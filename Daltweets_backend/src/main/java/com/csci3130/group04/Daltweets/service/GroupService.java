package com.csci3130.group04.Daltweets.service;

import java.util.List;

import com.csci3130.group04.Daltweets.model.Group;
import com.csci3130.group04.Daltweets.model.GroupMembers;
import com.csci3130.group04.Daltweets.model.User;

import java.util.List;

public interface GroupService {
    public Group getGroupByName( Group group );
    public boolean isValidToDelete(String username, String groupname);
    public Group createGroup(Group group);
    public List<Group> getAllGroups();
    public Group deleteGroup ( Group group );
    public List<Group> getGroupsByUser( String username );
    public List<User> getGroupMembers(String groupName);
    public List<User> getGroupAdmins(String groupName);
    public GroupMembers deleteUser(String username, String groupname);
}
