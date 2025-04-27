package com.tracktopiasapi.one.web.controller;

import com.tracktopiasapi.one.config.UserDetailsImpl;
import com.tracktopiasapi.one.model.Habit;
import com.tracktopiasapi.one.model.User;
import com.tracktopiasapi.one.services.HabitService;
import com.tracktopiasapi.one.web.dto.HabitCreationDto;
import com.tracktopiasapi.one.web.dto.HabitDto;
import com.tracktopiasapi.one.model.mapper.HabitMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/habits")
@RequiredArgsConstructor
public class HabitController {
    private final HabitService habitService;
    private final HabitMapper habitMapper;

    @GetMapping
    public ResponseEntity<List<HabitDto>> getAllHabitsByUserId(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        var habits = habitService.getAllHabitsByUserId(userDetails.getId());
        return ResponseEntity.ok(habitMapper.toHabitDTOList(habits));
    }

    @GetMapping("/{id}")
    public ResponseEntity<HabitDto> getHabitById(@PathVariable Long id) {
        return ResponseEntity.of(habitService.getHabitById(id).map(habitMapper::toHabitDTO));
    }

    @PostMapping
    public ResponseEntity<HabitDto> createHabit(
            @RequestBody HabitCreationDto habitDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Habit habit = habitMapper.toHabit(habitDto);
        Habit createdHabit = habitService.createHabit(habit, userDetails);
        return ResponseEntity.ok(habitMapper.toHabitDTO(createdHabit));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HabitDto> updateHabit(
            @PathVariable Long id,
            @RequestBody HabitCreationDto habitDto) {
        Habit habitDetails = habitMapper.toHabit(habitDto);
        return ResponseEntity.of(
                habitService.updateHabit(id, habitDetails).map(habitMapper::toHabitDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHabit(@PathVariable Long id) {
        if (habitService.deleteHabit(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
