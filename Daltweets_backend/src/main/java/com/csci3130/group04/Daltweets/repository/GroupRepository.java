package com.csci3130.group04.Daltweets.repository;

import com.csci3130.group04.Daltweets.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<Group,Integer> {
    Group findGroupByName(String GroupName);

    @Query("SELECT COUNT(*) FROM Group WHERE name = :name")
    int getGroupCount(@Param("name") String name);
}
