package com.tracktopiasapi.one.services.impl;

import com.tracktopiasapi.one.config.UserDetailsImpl;
import com.tracktopiasapi.one.model.Habit;
import com.tracktopiasapi.one.model.User;
import com.tracktopiasapi.one.repository.HabitRepository;
import com.tracktopiasapi.one.repository.UserRepository;
import com.tracktopiasapi.one.services.HabitService;
import com.tracktopiasapi.one.services.LevelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HabitServiceImpl implements HabitService {

    private final HabitRepository habitRepository;
    private final UserRepository userRepository;
    private final LevelService levelService;

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
    public Habit createHabit(Habit habit, UserDetailsImpl userDetails) {
        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        habit.setUser(user);
        return habitRepository.save(habit);
    }

    @Override
    @Transactional
    public Optional<Habit> updateHabit(Long id, Habit habitDetails) {
        return habitRepository.findById(id)
                .map(existingHabit -> {
                    boolean wasCompleted = existingHabit.isCompleted();
                    boolean isCompleted = habitDetails.isCompleted();
                    
                    existingHabit.setTitle(habitDetails.getTitle());
                    existingHabit.setDescription(habitDetails.getDescription());
                    existingHabit.setWeekdays(habitDetails.getWeekdays());
                    existingHabit.setCompleted(isCompleted);
                    
                    if (wasCompleted != isCompleted) {
                        User user = existingHabit.getUser();
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
