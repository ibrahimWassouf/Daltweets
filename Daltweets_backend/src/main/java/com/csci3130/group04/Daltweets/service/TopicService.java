package com.csci3130.group04.Daltweets.service;

import com.csci3130.group04.Daltweets.model.Topic;

import java.util.List;

public interface TopicService {
    public Topic createTopic( String name );

    public List<Topic> getAllTopics();

    public Topic getTopicByName( String name);
}
