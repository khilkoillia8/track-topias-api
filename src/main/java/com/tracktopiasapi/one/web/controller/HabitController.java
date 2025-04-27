package com.tracktopiasapi.one.web.controller;

import com.tracktopiasapi.one.config.UserDetailsImpl;
import com.tracktopiasapi.one.model.Habit;
import com.tracktopiasapi.one.services.HabitService;
import com.tracktopiasapi.one.web.dto.HabitCreationDto;
import com.tracktopiasapi.one.web.dto.HabitDto;
import com.tracktopiasapi.one.model.mapper.HabitMapper;
import jakarta.validation.Valid;
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
}
