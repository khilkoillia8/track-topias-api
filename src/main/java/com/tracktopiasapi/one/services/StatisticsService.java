package com.tracktopiasapi.one.services;

import com.tracktopiasapi.one.web.dto.statistics.CompletedHabitsByTopicDto;
import com.tracktopiasapi.one.web.dto.statistics.CompletedMissionsByTopicDto;
import com.tracktopiasapi.one.web.dto.statistics.UserStatisticsDto;

import java.util.List;

public interface StatisticsService {
    /**
     * Get all completed habits statistics grouped by topic for a specific user
     * @param userId User ID
     * @return List of completed habits by topic
     */
    List<CompletedHabitsByTopicDto> getCompletedHabitsByTopic(Long userId);
    
    /**
     * Get all completed missions statistics grouped by topic for a specific user
     * @param userId User ID
     * @return List of completed missions by topic
     */
    List<CompletedMissionsByTopicDto> getCompletedMissionsByTopic(Long userId);
    
    /**
     * Get complete user statistics including habits and missions completion
     * @param userId User ID
     * @return User statistics
     */
    UserStatisticsDto getUserStatistics(Long userId);
}
