package com.csci3130.group04.Daltweets.service.Implementation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csci3130.group04.Daltweets.model.Group;
import com.csci3130.group04.Daltweets.model.GroupMembers;
import com.csci3130.group04.Daltweets.model.User;
import com.csci3130.group04.Daltweets.repository.GroupMembersRepository;
import com.csci3130.group04.Daltweets.repository.GroupRepository;
import com.csci3130.group04.Daltweets.service.GroupService;

@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    GroupRepository groupRepository;
    @Autowired
    UserServiceImplementation userService;
    @Autowired
    GroupMembersRepository groupMembersRepository;
    @Override
    public Group getGroupByName(Group group) {
        if ( group == null ) {
            throw new IllegalArgumentException("group is NULL to get");
        }
        String groupName = group.getName();
        Group group_found = groupRepository.findGroupByName(groupName);
        return group_found;
    }

    @Override
    public boolean isValidToDelete(String username, String groupname) {
        List<GroupMembers> groupMembers = groupMembersRepository.findGroupMembersByUsername(username);
        boolean valid = false;
        for ( int i = 0 ; i < groupMembers.size() ; ++i ) {
            GroupMembers groupMember = groupMembers.get(i);
            if ( groupMember.getGroup().getName().equals(groupname) && groupMember.isAdmin() == true ) valid = true;
        }
        return valid;
    }
    @Override
    public Group deleteGroup(Group group) {
        if ( group == null ) {
            throw new IllegalArgumentException("group is NULL to delete");
        }

        Group group_found = getGroupByName(group);
        group_found.setIsDeleted(true);
        return groupRepository.save(group_found);
    }

    @Override
    public Group createGroup(Group group){
        if (group == null){
            return null;
        }

        if (group.getDateCreated() == null){
            group.setDateCreated(LocalDateTime.now());
        }

        if (group.getName() == null){
            return null;
        }

        return groupRepository.save(group);
    }

    @Override
    public List<Group> getGroupsByUser( String username ) {
        if ( username == null ) {
            throw new IllegalArgumentException("username is NULL");
        }
        List<GroupMembers> groupMembers = groupMembersRepository.findGroupMembersByUsername(username);
        List<Group> groups = new ArrayList<>();
        for ( int i = 0 ; i < groupMembers.size() ; ++i ) {
            GroupMembers groupMember = groupMembers.get(i);
            groups.add(groupMember.getGroup());
        }
        return groups;
    }

    @Override
    public List<User> getGroupMembers(String groupName) {
       if (groupName == null || groupName.isBlank()) return null;
       Group foundGroup = groupRepository.findGroupByName(groupName);
       if (foundGroup == null) return null;
       return groupMembersRepository.findGroupMembersByGroupName(foundGroup.getName());
    }

    @Override
    public List<User> getGroupAdmins(String groupName) {
       if (groupName == null || groupName.isBlank()) return null;
       Group foundGroup = groupRepository.findGroupByName(groupName);
       if (foundGroup == null) return null;
       return groupMembersRepository.findAdminsByGroupName(groupName);
    }

    @Override
    public List<Group> getAllGroups(){
      return groupRepository.findAll();
    }

    @Override
    public GroupMembers addUser(String username, String groupname, boolean isAdmin){

        if(username == null){
            throw new IllegalArgumentException("User is null");
        }
        if(groupname == null){
            throw new IllegalArgumentException("Group is null");
        }

        User user = userService.getUserByName(username);
        Group group = groupRepository.findGroupByName(groupname);

        if(user == null){
            throw new IllegalArgumentException("User was not found");
        }
        if(group == null){
            throw new IllegalArgumentException("Group was not found");
        }

        if(group.getIsDeleted()){
            throw new IllegalArgumentException("Cannot add user to deleted group");
        }

        GroupMembers alreadyExisting = groupMembersRepository.findGroupMembersByUserAndGroup(username, groupname);

        if(alreadyExisting != null){
            return null;
        }

        GroupMembers addedUser = new GroupMembers();
        addedUser.setUser(user);
        addedUser.setGroup(group);
        addedUser.setAdmin(isAdmin);

        return groupMembersRepository.save(addedUser);
    }

    @Override
    public GroupMembers deleteUser(String username, String groupname) {
        if ( !userService.isValidName(username) && !userService.isValidName(groupname) ) return null;

        GroupMembers groupMembers = groupMembersRepository.findGroupMembersByUserAndGroup(username,groupname);

        if ( groupMembers == null ) {
            return null;
        }
        groupMembersRepository.delete(groupMembers);
        return groupMembers;
    }

    @Override
    public int getGroupCount(String name){
      return groupRepository.getGroupCount(name);
    }
}
