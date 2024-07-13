package com.csci3130.group04.Daltweets.service;

import com.csci3130.group04.Daltweets.model.Group;

public interface GroupService {
    public Group getGroupByName( Group group );
    public boolean isValidToDelete(String username, String groupname);
    public Group deleteGroup ( Group group );
}
