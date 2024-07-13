package com.csci3130.group04.Daltweets.repository;

import com.csci3130.group04.Daltweets.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<Group,Integer> {
    Group findGroupById(int GroupId );
    Group findGroupByName(String GroupName);
}
