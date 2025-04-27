package com.tracktopiasapi.one.services.impl;

import com.tracktopiasapi.one.config.UserDetailsImpl;
import com.tracktopiasapi.one.model.Topic;
import com.tracktopiasapi.one.model.User;
import com.tracktopiasapi.one.repository.TopicRepository;
import com.tracktopiasapi.one.repository.UserRepository;
import com.tracktopiasapi.one.services.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TopicServiceImpl implements TopicService {

    private final TopicRepository topicRepository;
    private final UserRepository userRepository;

    @Override
    public List<Topic> getAllTopics() {
        return topicRepository.findAll();
    }

    @Override
    public Optional<Topic> getTopicById(Long id) {
        return topicRepository.findById(id);
    }

    @Override
    @Transactional
    public Topic createTopic(Topic topic, UserDetailsImpl userDetails) {
        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (topicRepository.existsByNameAndUserId(topic.getName(), user.getId())) {
            throw new RuntimeException("Topic with name '" + topic.getName() + "' already exists for this user");
        }
        
        topic.setUser(user);
        return topicRepository.save(topic);
    }

    @Override
    @Transactional
    public Optional<Topic> updateTopic(Long id, Topic topicDetails) {
        return topicRepository.findById(id)
                .map(existingTopic -> {
                    if (!existingTopic.getName().equals(topicDetails.getName()) && 
                        topicRepository.existsByNameAndUserId(topicDetails.getName(), existingTopic.getUser().getId())) {
                        throw new RuntimeException("Topic with name '" + topicDetails.getName() + "' already exists for this user");
                    }
                    
                    existingTopic.setName(topicDetails.getName());
                    existingTopic.setColor(topicDetails.getColor());
                    return topicRepository.save(existingTopic);
                });
    }

    @Override
    @Transactional
    public boolean deleteTopic(Long id) {
        return topicRepository.findById(id)
                .map(topic -> {
                    if (!topic.getMissions().isEmpty() || !topic.getHabits().isEmpty()) {
                        throw new RuntimeException("Cannot delete topic that is still being used");
                    }
                    topicRepository.delete(topic);
                    return true;
                })
                .orElse(false);
    }

    @Override
    public List<Topic> getAllTopicsByUserId(Long id) {
        return topicRepository.findAllByUserId(id);
    }
    
    @Override
    public Set<Topic> getTopicsByIds(Set<Long> topicIds, Long userId) {
        if (topicIds == null || topicIds.isEmpty()) {
            return new HashSet<>();
        }
        
        List<Topic> topics = topicRepository.findAllById(topicIds);
        
        return topics.stream()
                .filter(topic -> topic.getUser().getId().equals(userId))
                .collect(Collectors.toSet());
    }
    
    @Override
    public boolean validateTopicsExistAndBelongToUser(Set<Long> topicIds, Long userId) {
        if (topicIds == null || topicIds.isEmpty()) {
            return false;
        }
        
        Set<Topic> topics = getTopicsByIds(topicIds, userId);
        
        return topics.size() == topicIds.size();
    }
}
