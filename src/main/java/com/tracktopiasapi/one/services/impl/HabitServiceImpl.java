package com.tracktopiasapi.one.services.impl;

import com.tracktopiasapi.one.config.UserDetailsImpl;
import com.tracktopiasapi.one.model.Habit;
import com.tracktopiasapi.one.model.Topic;
import com.tracktopiasapi.one.model.User;
import com.tracktopiasapi.one.repository.HabitRepository;
import com.tracktopiasapi.one.repository.UserRepository;
import com.tracktopiasapi.one.services.HabitService;
import com.tracktopiasapi.one.services.LevelService;
import com.tracktopiasapi.one.services.TopicService;
import com.tracktopiasapi.one.web.dto.HabitCreationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class HabitServiceImpl implements HabitService {

    private final HabitRepository habitRepository;
    private final UserRepository userRepository;
    private final LevelService levelService;
    private final TopicService topicService;

    @Override
    public List<Habit> getAllHabits() {
        return habitRepository.findAll();
    }

    @Override
    public Optional<Habit> getHabitById(Long id) {
        return habitRepository.findById(id);
    }

    @Override
    @Transactional
    public Habit createHabit(Habit habit, HabitCreationDto habitDto, UserDetailsImpl userDetails) {
        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (habitDto.getTopicIds() == null || habitDto.getTopicIds().isEmpty()) {
            throw new RuntimeException("At least one topic is required");
        }
        
        if (!topicService.validateTopicsExistAndBelongToUser(habitDto.getTopicIds(), user.getId())) {
            throw new RuntimeException("One or more topics do not exist or do not belong to user");
        }
        
        Set<Topic> topics = topicService.getTopicsByIds(habitDto.getTopicIds(), user.getId());
        habit.setUser(user);
        habit.setTopics(topics);
        
        return habitRepository.save(habit);
    }

    @Override
    @Transactional
    public Optional<Habit> updateHabit(Long id, Habit habitDetails, Set<Long> topicIds) {
        return habitRepository.findById(id)
                .map(existingHabit -> {
                    boolean wasCompleted = existingHabit.isCompleted();
                    boolean isCompleted = habitDetails.isCompleted();
                    User user = existingHabit.getUser();
                    
                    if (topicIds != null && !topicIds.isEmpty()) {
                        if (!topicService.validateTopicsExistAndBelongToUser(topicIds, user.getId())) {
                            throw new RuntimeException("One or more topics do not exist or do not belong to user");
                        }
                        Set<Topic> topics = topicService.getTopicsByIds(topicIds, user.getId());
                        existingHabit.setTopics(topics);
                    }
                    
                    existingHabit.setTitle(habitDetails.getTitle());
                    existingHabit.setDescription(habitDetails.getDescription());
                    existingHabit.setWeekdays(habitDetails.getWeekdays());
                    existingHabit.setCompleted(isCompleted);
                    
                    if (wasCompleted != isCompleted) {
                        if (isCompleted) {
                            levelService.addPoints(user, LevelServiceImpl.HABIT_COMPLETION_POINTS);
                        } else {
                            levelService.removePoints(user, LevelServiceImpl.HABIT_COMPLETION_POINTS);
                        }
                    }
                    
                    return habitRepository.save(existingHabit);
                });
    }

    @Override
    @Transactional
    public boolean deleteHabit(Long id) {
        return habitRepository.findById(id)
                .map(habit -> {
                    if (habit.isCompleted()) {
                        levelService.removePoints(habit.getUser(), LevelServiceImpl.HABIT_COMPLETION_POINTS);
                    }
                    habitRepository.delete(habit);
                    return true;
                })
                .orElse(false);
    }

    @Override
    public List<Habit> getAllHabitsByUserId(Long id) {
        return habitRepository.findAllByUserId(id);
    }
}
