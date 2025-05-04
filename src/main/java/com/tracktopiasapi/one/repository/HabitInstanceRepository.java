package com.tracktopiasapi.one.repository;

import com.tracktopiasapi.one.model.HabitInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Repository
public interface HabitInstanceRepository extends JpaRepository<HabitInstance, Long> {
    List<HabitInstance> findByHabitId(Long habitId);
    List<HabitInstance> findByHabitIdAndActive(Long habitId, boolean active);
    List<HabitInstance> findByHabitIdAndDate(Long habitId, LocalDate date);
    List<HabitInstance> findByHabitIdAndWeekday(Long habitId, String weekday);
    List<HabitInstance> findByHabitUserId(Long userId);
    List<HabitInstance> findByHabitUserIdAndActive(Long userId, boolean active);
    List<HabitInstance> findByHabitUserIdAndDate(Long userId, LocalDate date);
    List<HabitInstance> findByHabitUserIdAndDateBetween(Long userId, LocalDate startDate, LocalDate endDate);
    List<HabitInstance> findByHabitIdAndDateBetween(Long habitId, LocalDate today, LocalDate tomorrow);
}
