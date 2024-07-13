package com.csci3130.group04.Daltweets.service.Implementation;

import com.csci3130.group04.Daltweets.model.Group;
import com.csci3130.group04.Daltweets.model.GroupMembers;
import com.csci3130.group04.Daltweets.repository.GroupMembersRepository;
import com.csci3130.group04.Daltweets.repository.GroupRepository;
import com.csci3130.group04.Daltweets.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    GroupRepository groupRepository;
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
}
