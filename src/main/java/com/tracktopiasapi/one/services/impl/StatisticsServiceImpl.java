package com.tracktopiasapi.one.services.impl;

import com.tracktopiasapi.one.model.Habit;
import com.tracktopiasapi.one.model.Mission;
import com.tracktopiasapi.one.model.Topic;
import com.tracktopiasapi.one.model.User;
import com.tracktopiasapi.one.repository.HabitRepository;
import com.tracktopiasapi.one.repository.MissionRepository;
import com.tracktopiasapi.one.repository.UserRepository;
import com.tracktopiasapi.one.services.StatisticsService;
import com.tracktopiasapi.one.web.dto.statistics.CompletedHabitsByTopicDto;
import com.tracktopiasapi.one.web.dto.statistics.CompletedMissionsByTopicDto;
import com.tracktopiasapi.one.web.dto.statistics.UserStatisticsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final HabitRepository habitRepository;
    private final MissionRepository missionRepository;
    private final UserRepository userRepository;

    @Override
    public List<CompletedHabitsByTopicDto> getCompletedHabitsByTopic(Long userId) {
        List<Habit> habits = habitRepository.findAllByUserId(userId);
        
        Map<Topic, Long> topicCounts = calculateTopicCounts(habits, Habit::isCompleted);
        
        return topicCounts.entrySet().stream()
                .map(entry -> CompletedHabitsByTopicDto.builder()
                        .topicId(entry.getKey().getId())
                        .topicName(entry.getKey().getName())
                        .completedCount(entry.getValue())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<CompletedMissionsByTopicDto> getCompletedMissionsByTopic(Long userId) {
        List<Mission> missions = missionRepository.findAllByUserId(userId);
        
        Map<Topic, Long> topicCounts = calculateTopicCounts(missions, Mission::isCompleted);
        
        return topicCounts.entrySet().stream()
                .map(entry -> CompletedMissionsByTopicDto.builder()
                        .topicId(entry.getKey().getId())
                        .topicName(entry.getKey().getName())
                        .completedCount(entry.getValue())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public UserStatisticsDto getUserStatistics(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        List<Habit> habits = habitRepository.findAllByUserId(userId);
        List<Mission> missions = missionRepository.findAllByUserId(userId);
        
        long totalHabits = habits.size();
        long completedHabits = habits.stream().filter(Habit::isCompleted).count();
        
        long totalMissions = missions.size();
        long completedMissions = missions.stream().filter(Mission::isCompleted).count();
        
        List<CompletedHabitsByTopicDto> habitsByTopic = getCompletedHabitsByTopic(userId);
        List<CompletedMissionsByTopicDto> missionsByTopic = getCompletedMissionsByTopic(userId);
        
        return UserStatisticsDto.builder()
                .userId(userId)
                .username(user.getUsername())
                .totalHabits(totalHabits)
                .completedHabits(completedHabits)
                .totalMissions(totalMissions)
                .completedMissions(completedMissions)
                .habitsByTopic(habitsByTopic)
                .missionsByTopic(missionsByTopic)
                .build();
    }
    
    private <T> Map<Topic, Long> calculateTopicCounts(List<T> items, Function<T, Boolean> isCompletedFunction) {
        Map<Topic, Long> topicCounts = new HashMap<>();
        
        for (T item : items) {
            if (item instanceof Habit) {
                Habit habit = (Habit) item;
                if (isCompletedFunction.apply(item)) {
                    for (Topic topic : habit.getTopics()) {
                        topicCounts.put(topic, topicCounts.getOrDefault(topic, 0L) + 1);
                    }
                }
            } else if (item instanceof Mission) {
                Mission mission = (Mission) item;
                if (isCompletedFunction.apply(item)) {
                    for (Topic topic : mission.getTopics()) {
                        topicCounts.put(topic, topicCounts.getOrDefault(topic, 0L) + 1);
                    }
                }
            }
        }
        
        return topicCounts;
    }
}
