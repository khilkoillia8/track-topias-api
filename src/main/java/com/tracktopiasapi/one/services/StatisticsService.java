package com.tracktopiasapi.one.services;

import com.tracktopiasapi.one.web.dto.statistics.CompletedHabitsByTopicDto;
import com.tracktopiasapi.one.web.dto.statistics.CompletedMissionsByTopicDto;
import com.tracktopiasapi.one.web.dto.statistics.HabitInstanceStatisticsDto;
import com.tracktopiasapi.one.web.dto.statistics.UserHabitInstanceStatisticsDto;
import com.tracktopiasapi.one.web.dto.statistics.UserRatingDto;
import com.tracktopiasapi.one.web.dto.statistics.UserStatisticsDto;

import java.util.List;

public interface StatisticsService {
    List<CompletedHabitsByTopicDto> getCompletedHabitsByTopic(Long userId);
    
    List<CompletedMissionsByTopicDto> getCompletedMissionsByTopic(Long userId);
    
    UserStatisticsDto getUserStatistics(Long userId);
    
    List<HabitInstanceStatisticsDto> getHabitInstanceStatisticsByHabit(Long userId);
    
    HabitInstanceStatisticsDto getHabitInstanceStatisticsForHabit(Long habitId);
    
    UserHabitInstanceStatisticsDto getUserHabitInstanceStatistics(Long userId);
    
    List<UserRatingDto> getUserRatingsByLevel();
    
    List<UserRatingDto> getUserRatingsByMissionsCompleted();
    
    List<UserRatingDto> getUserRatingsByMaxHabitStreak();
    
    List<UserRatingDto> getUserRatingsByHabitCompletions();
}
