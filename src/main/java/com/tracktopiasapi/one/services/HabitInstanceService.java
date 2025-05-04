package com.tracktopiasapi.one.services;

import com.tracktopiasapi.one.model.HabitInstance;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface HabitInstanceService {
    
    List<HabitInstance> getHabitInstancesByHabitId(Long habitId);
    
    List<HabitInstance> getActiveHabitInstancesByHabitId(Long habitId);
    
    List<HabitInstance> getHabitInstancesByHabitIdAndDate(Long habitId, LocalDate date);
    
    List<HabitInstance> getHabitInstancesByUserId(Long userId);
    
    List<HabitInstance> getActiveHabitInstancesByUserId(Long userId);
    
    List<HabitInstance> getHabitInstancesByUserIdAndDate(Long userId, LocalDate date);
    
    List<HabitInstance> getHabitInstancesByUserIdAndDateRange(Long userId, LocalDate startDate, LocalDate endDate);
    
    Optional<HabitInstance> getHabitInstanceById(Long id);
    
    HabitInstance completeHabitInstance(Long id);
    
    void generateHabitInstances(Long habitId);
    
    void deleteHabitInstancesByHabitId(Long habitId);
    
    void updateHabitStreaks(Long habitId);
    
    void checkMissedHabits();
}
