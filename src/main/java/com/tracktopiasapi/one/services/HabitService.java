package com.tracktopiasapi.one.services;

import com.tracktopiasapi.one.config.UserDetailsImpl;
import com.tracktopiasapi.one.model.Habit;
import com.tracktopiasapi.one.model.User;

import java.util.List;
import java.util.Optional;

public interface HabitService {
    
    List<Habit> getAllHabits();
    
    Optional<Habit> getHabitById(Long id);
    
    Habit createHabit(Habit habit, UserDetailsImpl userDetails);
    
    Optional<Habit> updateHabit(Long id, Habit habitDetails);
    
    boolean deleteHabit(Long id);

    List<Habit> getAllHabitsByUserId(Long id);
}
