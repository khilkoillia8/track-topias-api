package com.tracktopiasapi.one.repository;

import com.tracktopiasapi.one.model.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {
    List<Topic> findAllByUserId(Long userId);
    
    boolean existsByNameAndUserId(String name, Long userId);
}
