package com.tracktopiasapi.one.services;

import com.tracktopiasapi.one.config.UserDetailsImpl;
import com.tracktopiasapi.one.model.Habit;
import com.tracktopiasapi.one.web.dto.HabitCreationDto;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface HabitService {
    
    List<Habit> getAllHabits();
    
    Optional<Habit> getHabitById(Long id);
    
    Habit createHabit(Habit habit, HabitCreationDto habitDto, UserDetailsImpl userDetails);
    
    Optional<Habit> updateHabit(Long id, Habit habitDetails, Set<Long> topicIds);
    
    boolean deleteHabit(Long id);

    List<Habit> getAllHabitsByUserId(Long id);
}
