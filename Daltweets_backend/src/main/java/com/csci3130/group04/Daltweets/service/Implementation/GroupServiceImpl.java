package com.csci3130.group04.Daltweets.service.Implementation;

import com.csci3130.group04.Daltweets.model.Group;
import com.csci3130.group04.Daltweets.repository.GroupRepository;
import com.csci3130.group04.Daltweets.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    GroupRepository groupRepository;
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
    public Group deleteGroup(Group group) {
        if ( group == null ) {
            throw new IllegalArgumentException("group is NULL to delete");
        }

        Group group_found = getGroupByName(group);
        group_found.setIsDeleted(false);
        groupRepository.save(group_found);
        return group_found;
    }
}
