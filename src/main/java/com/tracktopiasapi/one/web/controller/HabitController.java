package com.tracktopiasapi.one.web.controller;

import com.tracktopiasapi.one.config.UserDetailsImpl;
import com.tracktopiasapi.one.model.Habit;
import com.tracktopiasapi.one.services.HabitService;
import com.tracktopiasapi.one.web.dto.HabitCreationDto;
import com.tracktopiasapi.one.web.dto.HabitDto;
import com.tracktopiasapi.one.model.mapper.HabitMapper;
import com.tracktopiasapi.one.services.HabitInstanceService;
import com.tracktopiasapi.one.web.dto.MissionDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/habits")
@RequiredArgsConstructor
public class HabitController {
    private final HabitService habitService;
    private final HabitMapper habitMapper;
    private final HabitInstanceService habitInstanceService;

    @GetMapping
    public ResponseEntity<List<HabitDto>> getAllHabitsByUserId(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {

        var habits = habitService.getAllHabitsByUserId(userDetails.getId());
        System.out.println(habits);
        System.out.println(habitMapper.toHabitDTOList(habits));
        return ResponseEntity.ok(habitMapper.toHabitDTOList(habits));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<HabitDto>> getAllHabitsByUserId(
            @PathVariable Long id
    ) {
        var habits = habitService.getAllHabitsByUserId(id);
        return ResponseEntity.ok(habitMapper.toHabitDTOList(habits));
    }

    @GetMapping("/{id}")
    public ResponseEntity<HabitDto> getHabitById(@PathVariable Long id) {
        return ResponseEntity.of(habitService.getHabitById(id).map(habitMapper::toHabitDTO));
    }

    @PostMapping
    public ResponseEntity<HabitDto> createHabit(
            @RequestBody @Valid HabitCreationDto habitDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            Habit habit = habitMapper.toHabit(habitDto);
            Habit createdHabit = habitService.createHabit(habit, habitDto, userDetails);
            return ResponseEntity.ok(habitMapper.toHabitDTO(createdHabit));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<HabitDto> updateHabit(
            @PathVariable Long id,
            @RequestBody HabitCreationDto habitDto) {
        try {
            Habit habitDetails = habitMapper.toHabit(habitDto);
            return ResponseEntity.of(
                    habitService.updateHabit(id, habitDetails, habitDto.getTopicIds())
                            .map(habitMapper::toHabitDTO));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHabit(@PathVariable Long id) {
        if (habitService.deleteHabit(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
    
    @PostMapping("/{id}/reset")
    public ResponseEntity<HabitDto> resetHabit(@PathVariable Long id) {
        try {
            Habit habit = habitService.resetHabit(id);
            return ResponseEntity.ok(habitMapper.toHabitDTO(habit));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/{id}/regenerate")
    public ResponseEntity<HabitDto> regenerateHabitInstances(@PathVariable Long id) {
        try {
            Habit habit = habitService.regenerateHabitInstances(id);
            return ResponseEntity.ok(habitMapper.toHabitDTO(habit));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/{id}/streak")
    public ResponseEntity<Map<String, Integer>> getHabitStreak(@PathVariable Long id) {
        return ResponseEntity.of(habitService.getHabitById(id)
                .map(habit -> Map.of(
                    "currentStreak", habit.getCurrentStreak(),
                    "bestStreak", habit.getBestStreak()
                )));
    }
    
    @PostMapping("/{id}/update-streak")
    public ResponseEntity<HabitDto> updateHabitStreak(@PathVariable Long id) {
        try {
            habitInstanceService.updateHabitStreaks(id);
            return ResponseEntity.of(habitService.getHabitById(id).map(habitMapper::toHabitDTO));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
