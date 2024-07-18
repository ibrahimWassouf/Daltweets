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
    public GroupMembers addUser(GroupMembers groupMember){

        if(groupMember == null){
            throw new IllegalArgumentException("GroupMember is null");
        }
        if(groupMember.getUser() == null){
            throw new IllegalArgumentException("User is null");
        }
        if(groupMember.getGroup() == null){
            throw new IllegalArgumentException("Group is null");
        }

        Group group = groupMember.getGroup();
        User user = groupMember.getUser();

        if(user.isAccountDeleted()){
            throw new IllegalArgumentException("Cannot add deleted user to group");
        }
        if(group.getIsDeleted()){
            throw new IllegalArgumentException("Cannot add user to deleted group");
        }

        String username = groupMember.getUser().getUsername();
        String groupName = groupMember.getGroup().getName();
        GroupMembers alreadyExisting = groupMembersRepository.findGroupMembersByUserAndGroup(username, groupName);

        if(alreadyExisting != null){
            System.out.println("User already exists in the group");
            return null;
        }

        return groupMembersRepository.save(groupMember);
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
}
