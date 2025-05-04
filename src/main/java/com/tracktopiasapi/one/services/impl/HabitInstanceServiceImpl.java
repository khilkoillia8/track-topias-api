package com.tracktopiasapi.one.services.impl;

import com.tracktopiasapi.one.model.Habit;
import com.tracktopiasapi.one.model.HabitInstance;
import com.tracktopiasapi.one.model.User;
import com.tracktopiasapi.one.repository.HabitInstanceRepository;
import com.tracktopiasapi.one.repository.HabitRepository;
import com.tracktopiasapi.one.services.HabitInstanceService;
import com.tracktopiasapi.one.services.LevelService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HabitInstanceServiceImpl implements HabitInstanceService {

    private final HabitInstanceRepository habitInstanceRepository;
    private final HabitRepository habitRepository;
    private final LevelService levelService;

    @Override
    public List<HabitInstance> getHabitInstancesByHabitId(Long habitId) {
        return habitInstanceRepository.findByHabitId(habitId);
    }

    @Override
    public List<HabitInstance> getActiveHabitInstancesByHabitId(Long habitId) {
        return habitInstanceRepository.findByHabitIdAndActive(habitId, true);
    }

    @Override
    public List<HabitInstance> getHabitInstancesByHabitIdAndDate(Long habitId, LocalDate date) {
        return habitInstanceRepository.findByHabitIdAndDate(habitId, date);
    }

    @Override
    public List<HabitInstance> getHabitInstancesByUserId(Long userId) {
        return habitInstanceRepository.findByHabitUserId(userId);
    }

    @Override
    public List<HabitInstance> getActiveHabitInstancesByUserId(Long userId) {
        return habitInstanceRepository.findByHabitUserIdAndActive(userId, true);
    }

    @Override
    public List<HabitInstance> getHabitInstancesByUserIdAndDate(Long userId, LocalDate date) {
        return habitInstanceRepository.findByHabitUserIdAndDate(userId, date);
    }

    @Override
    public List<HabitInstance> getHabitInstancesByUserIdAndDateRange(Long userId, LocalDate startDate, LocalDate endDate) {
        return habitInstanceRepository.findByHabitUserIdAndDateBetween(userId, startDate, endDate);
    }

    @Override
    public Optional<HabitInstance> getHabitInstanceById(Long id) {
        return habitInstanceRepository.findById(id);
    }

    @Override
    @Transactional
    public HabitInstance completeHabitInstance(Long id) {
        HabitInstance habitInstance = habitInstanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Habit instance not found"));

        if (!habitInstance.isActive()) {
            throw new RuntimeException("Cannot complete inactive habit instance");
        }

        habitInstance.setCompleted(true);
        HabitInstance savedInstance = habitInstanceRepository.save(habitInstance);

        User user = savedInstance.getHabit().getUser();
        levelService.addPoints(user, LevelServiceImpl.HABIT_COMPLETION_POINTS);

        List<HabitInstance> activeInstances = habitInstanceRepository.findByHabitIdAndActive(habitInstance.getHabit().getId(), true);
        boolean allCompleted = activeInstances.stream().allMatch(HabitInstance::isCompleted);

        if (allCompleted) {
            Habit habit = habitInstance.getHabit();
            habit.setCompleted(true);
            habitRepository.save(habit);
        }

        return savedInstance;
    }

    @Override
    @Transactional
    public void generateHabitInstances(Long habitId) {
        Habit habit = habitRepository.findById(habitId)
                .orElseThrow(() -> new RuntimeException("Habit not found"));

        if (habit.isCompleted()) {
            return;
        }
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);

        List<LocalDate> existingDates = habitInstanceRepository
                .findByHabitIdAndDateBetween(habitId, today, tomorrow)
                .stream()
                .map(HabitInstance::getDate)
                .toList();

        List<HabitInstance> newInstances = new ArrayList<>();

        for (int i = 0; i <= 1; i++) {
            LocalDate date = today.plusDays(i);
            String weekday = date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.UK).toLowerCase();

            if (habit.getWeekdays().contains(weekday) && !existingDates.contains(date)) {
                HabitInstance instance = HabitInstance.builder()
                        .habit(habit)
                        .date(date)
                        .weekday(weekday)
                        .completed(false)
                        .active(true)
                        .build();

                newInstances.add(instance);
            }
        }

        habitInstanceRepository.saveAll(newInstances);
    }

    
    @Override
    @Transactional
    public void deleteHabitInstancesByHabitId(Long habitId) {
        List<HabitInstance> instances = habitInstanceRepository.findByHabitId(habitId);
        
        for (HabitInstance instance : instances) {
            if (instance.isCompleted()) {
                User user = instance.getHabit().getUser();
                levelService.removePoints(user, LevelServiceImpl.HABIT_COMPLETION_POINTS);
            }
        }
        
        habitInstanceRepository.deleteAll(instances);
    }
    
    @Override
    @Transactional
    public void updateHabitStreaks(Long habitId) {
        Habit habit = habitRepository.findById(habitId)
                .orElseThrow(() -> new RuntimeException("Habit not found"));
        
        List<HabitInstance> instances = habitInstanceRepository.findByHabitId(habitId);
        
        List<HabitInstance> sortedInstances = instances.stream()
                .sorted(Comparator.comparing(HabitInstance::getDate).reversed())
                .toList();
        
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        
        boolean todayCompleted = sortedInstances.stream()
                .filter(instance -> instance.getDate().equals(today))
                .anyMatch(HabitInstance::isCompleted);
        
        boolean yesterdayCompleted = sortedInstances.stream()
                .filter(instance -> instance.getDate().equals(yesterday))
                .anyMatch(HabitInstance::isCompleted);
                
        boolean yesterdayScheduled = habit.getWeekdays().contains(
                yesterday.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.UK).toUpperCase());
        
        if (todayCompleted) {
            if (yesterdayCompleted || !yesterdayScheduled || habit.getCurrentStreak() == 0) {
                habit.increaseStreak();
            }
        } else if (habit.getCurrentStreak() > 0) {
            boolean missedAny = sortedInstances.stream()
                    .filter(instance -> instance.getDate().isBefore(today) && 
                            instance.isActive() && 
                            !instance.isCompleted())
                    .anyMatch(instance -> habit.getWeekdays().contains(
                            instance.getDate().getDayOfWeek()
                                    .getDisplayName(TextStyle.FULL, Locale.ENGLISH)
                                    .toUpperCase()));
            
            if (missedAny) {
                habit.resetStreak();
            }
        }
        
        habitRepository.save(habit);
    }
    
    @Override
    @Scheduled(cron = "0 0 0 * * *") 
    @Transactional
    public void checkMissedHabits() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        
        List<Habit> habits = habitRepository.findAll();
        
        for (Habit habit : habits) {
            String yesterdayName = yesterday.getDayOfWeek()
                .getDisplayName(TextStyle.FULL, Locale.ENGLISH)
                .toUpperCase();
                
            if (habit.getWeekdays().contains(yesterdayName)) {
                List<HabitInstance> yesterdayInstances = habitInstanceRepository
                        .findByHabitIdAndDate(habit.getId(), yesterday);
                
                boolean yesterdayCompleted = !yesterdayInstances.isEmpty() && 
                    yesterdayInstances.stream().anyMatch(HabitInstance::isCompleted);
                
                if (!yesterdayCompleted && habit.getCurrentStreak() > 0) {
                    habit.resetStreak();
                    habitRepository.save(habit);
                }
            }
        }
    }
}
