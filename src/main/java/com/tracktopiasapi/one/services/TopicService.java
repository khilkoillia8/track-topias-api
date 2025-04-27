package com.tracktopiasapi.one.services;

import com.tracktopiasapi.one.config.UserDetailsImpl;
import com.tracktopiasapi.one.model.Topic;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TopicService {
    
    List<Topic> getAllTopics();
    
    Optional<Topic> getTopicById(Long id);
    
    Topic createTopic(Topic topic, UserDetailsImpl userDetails);
    
    Optional<Topic> updateTopic(Long id, Topic topicDetails);
    
    boolean deleteTopic(Long id);

    List<Topic> getAllTopicsByUserId(Long id);
    
    Set<Topic> getTopicsByIds(Set<Long> topicIds, Long userId);
    
    boolean validateTopicsExistAndBelongToUser(Set<Long> topicIds, Long userId);
}
