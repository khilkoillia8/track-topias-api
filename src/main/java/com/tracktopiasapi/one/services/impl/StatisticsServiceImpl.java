package com.tracktopiasapi.one.services.impl;

import com.tracktopiasapi.one.model.Habit;
import com.tracktopiasapi.one.model.HabitInstance;
import com.tracktopiasapi.one.model.Mission;
import com.tracktopiasapi.one.model.Topic;
import com.tracktopiasapi.one.model.User;
import com.tracktopiasapi.one.repository.HabitInstanceRepository;
import com.tracktopiasapi.one.repository.HabitRepository;
import com.tracktopiasapi.one.repository.MissionRepository;
import com.tracktopiasapi.one.repository.UserRepository;
import com.tracktopiasapi.one.services.StatisticsService;
import com.tracktopiasapi.one.web.dto.statistics.CompletedHabitsByTopicDto;
import com.tracktopiasapi.one.web.dto.statistics.CompletedMissionsByTopicDto;
import com.tracktopiasapi.one.web.dto.statistics.HabitInstanceStatisticsDto;
import com.tracktopiasapi.one.web.dto.statistics.UserHabitInstanceStatisticsDto;
import com.tracktopiasapi.one.web.dto.statistics.UserRatingDto;
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
    private final HabitInstanceRepository habitInstanceRepository;

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
    
    @Override
    public List<HabitInstanceStatisticsDto> getHabitInstanceStatisticsByHabit(Long userId) {
        List<Habit> habits = habitRepository.findAllByUserId(userId);
        
        return habits.stream()
                .map(habit -> getHabitInstanceStatisticsForHabit(habit.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public HabitInstanceStatisticsDto getHabitInstanceStatisticsForHabit(Long habitId) {
        Habit habit = habitRepository.findById(habitId)
                .orElseThrow(() -> new RuntimeException("Habit not found"));
        
        List<HabitInstance> instances = habitInstanceRepository.findByHabitId(habitId);
        
        long totalInstances = instances.size();
        long completedInstances = instances.stream().filter(HabitInstance::isCompleted).count();
        
        double completionRate = totalInstances > 0 
                ? (double) completedInstances / totalInstances * 100.0 
                : 0.0;
        
        return HabitInstanceStatisticsDto.builder()
                .habitId(habit.getId())
                .habitTitle(habit.getTitle())
                .totalInstances(totalInstances)
                .completedInstances(completedInstances)
                .completionRate(Math.round(completionRate * 100.0) / 100.0)
                .currentStreak(habit.getCurrentStreak())
                .bestStreak(habit.getBestStreak())
                .build();
    }

    @Override
    public UserHabitInstanceStatisticsDto getUserHabitInstanceStatistics(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        List<HabitInstance> allUserInstances = habitInstanceRepository.findByHabitUserId(userId);
        
        long totalInstances = allUserInstances.size();
        long completedInstances = allUserInstances.stream().filter(HabitInstance::isCompleted).count();
        
        double overallCompletionRate = totalInstances > 0 
                ? (double) completedInstances / totalInstances * 100.0 
                : 0.0;
        
        List<HabitInstanceStatisticsDto> habitInstanceStats = getHabitInstanceStatisticsByHabit(userId);
        
        return UserHabitInstanceStatisticsDto.builder()
                .userId(userId)
                .username(user.getUsername())
                .totalHabitInstances(totalInstances)
                .completedHabitInstances(completedInstances)
                .overallCompletionRate(Math.round(overallCompletionRate * 100.0) / 100.0)
                .habitInstancesByHabit(habitInstanceStats)
                .build();
    }

    @Override
    public List<UserRatingDto> getUserRatingsByLevel() {
        List<User> allUsers = userRepository.findAll();
        
        return allUsers.stream()
                .filter(user -> user.getLevel() != null)
                .sorted(Comparator.comparing((User user) -> user.getLevel().getLevel()).reversed()
                        .thenComparing(user -> user.getLevel().getScore(), Comparator.reverseOrder()))
                .map(user -> {
                    int ranking = allUsers.indexOf(user) + 1;
                    return UserRatingDto.builder()
                            .userId(user.getId())
                            .username(user.getUsername())
                            .ranking(ranking)
                            .value(user.getLevel().getLevel())
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<UserRatingDto> getUserRatingsByMissionsCompleted() {
        List<User> allUsers = userRepository.findAll();
        
        Map<User, Long> userCompletedMissionCount = new HashMap<>();
        
        for (User user : allUsers) {
            List<Mission> missions = missionRepository.findAllByUserId(user.getId());
            long completedCount = missions.stream().filter(Mission::isCompleted).count();
            userCompletedMissionCount.put(user, completedCount);
        }
        
        return allUsers.stream()
                .sorted(Comparator.comparing(user -> userCompletedMissionCount.getOrDefault(user, 0L), Comparator.reverseOrder()))
                .map(user -> {
                    int ranking = allUsers.indexOf(user) + 1;
                    return UserRatingDto.builder()
                            .userId(user.getId())
                            .username(user.getUsername())
                            .ranking(ranking)
                            .value(userCompletedMissionCount.getOrDefault(user, 0L).intValue())
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<UserRatingDto> getUserRatingsByMaxHabitStreak() {
        List<User> allUsers = userRepository.findAll();
        
        Map<User, Integer> userMaxStreakMap = new HashMap<>();
        
        for (User user : allUsers) {
            List<Habit> habits = habitRepository.findAllByUserId(user.getId());
            int maxStreak = habits.stream()
                    .mapToInt(Habit::getBestStreak)
                    .max()
                    .orElse(0);
            userMaxStreakMap.put(user, maxStreak);
        }
        
        return allUsers.stream()
                .sorted(Comparator.comparing(user -> userMaxStreakMap.getOrDefault(user, 0), Comparator.reverseOrder()))
                .map(user -> {
                    int ranking = allUsers.indexOf(user) + 1;
                    return UserRatingDto.builder()
                            .userId(user.getId())
                            .username(user.getUsername())
                            .ranking(ranking)
                            .value(userMaxStreakMap.getOrDefault(user, 0))
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<UserRatingDto> getUserRatingsByHabitCompletions() {
        List<User> allUsers = userRepository.findAll();
        
        Map<User, Long> userHabitCompletionsMap = new HashMap<>();
        
        for (User user : allUsers) {
            List<HabitInstance> instances = habitInstanceRepository.findByHabitUserId(user.getId());
            long completedCount = instances.stream().filter(HabitInstance::isCompleted).count();
            userHabitCompletionsMap.put(user, completedCount);
        }
        
        return allUsers.stream()
                .sorted(Comparator.comparing(user -> userHabitCompletionsMap.getOrDefault(user, 0L), Comparator.reverseOrder()))
                .map(user -> {
                    int ranking = allUsers.indexOf(user) + 1;
                    return UserRatingDto.builder()
                            .userId(user.getId())
                            .username(user.getUsername())
                            .ranking(ranking)
                            .value(userHabitCompletionsMap.getOrDefault(user, 0L).intValue())
                            .build();
                })
                .collect(Collectors.toList());
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
