package com.csci3130.group04.Daltweets.service.Implementation;

import com.csci3130.group04.Daltweets.model.Topic;
import com.csci3130.group04.Daltweets.repository.TopicRepository;
import com.csci3130.group04.Daltweets.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TopicServiceImpl implements TopicService {
    @Autowired
    TopicRepository topicRepository;

    @Override
    public Topic createTopic(String name) {
        if ( name == null ) return null;
        Topic found_topic = topicRepository.findTopicByName(name);
        if ( found_topic != null ) return found_topic;
        Topic topic = new Topic();
        topic.setName(name);
        return topicRepository.save(topic);
    }

    @Override
    public List<Topic> getAllTopics() {
        return topicRepository.findAll();
    }

    @Override
    public Topic getTopicByName(String name) {
        return topicRepository.findTopicByName(name);
    }
}
