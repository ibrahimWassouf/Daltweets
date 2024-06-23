package com.csci3130.group04.Daltweets.repository;

import com.csci3130.group04.Daltweets.model.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicRepository extends JpaRepository<Topic,Integer> {
}
