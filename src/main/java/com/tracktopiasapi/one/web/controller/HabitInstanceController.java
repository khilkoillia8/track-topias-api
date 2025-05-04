package com.tracktopiasapi.one.web.controller;

import com.tracktopiasapi.one.config.UserDetailsImpl;
import com.tracktopiasapi.one.model.HabitInstance;
import com.tracktopiasapi.one.model.mapper.HabitInstanceMapper;
import com.tracktopiasapi.one.services.HabitInstanceService;
import com.tracktopiasapi.one.web.dto.HabitInstanceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/habit-instances")
@RequiredArgsConstructor
public class HabitInstanceController {
    private final HabitInstanceService habitInstanceService;
    private final HabitInstanceMapper habitInstanceMapper;

    @GetMapping("/habit/{habitId}")
    public ResponseEntity<List<HabitInstanceDto>> getInstancesByHabitId(@PathVariable Long habitId) {
        List<HabitInstance> instances = habitInstanceService.getHabitInstancesByHabitId(habitId);
        return ResponseEntity.ok(habitInstanceMapper.toHabitInstanceDTOList(instances));
    }

    @GetMapping("/habit/{habitId}/active")
    public ResponseEntity<List<HabitInstanceDto>> getActiveInstancesByHabitId(@PathVariable Long habitId) {
        List<HabitInstance> instances = habitInstanceService.getActiveHabitInstancesByHabitId(habitId);
        return ResponseEntity.ok(habitInstanceMapper.toHabitInstanceDTOList(instances));
    }

    @GetMapping
    public ResponseEntity<List<HabitInstanceDto>> getUserInstances(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<HabitInstance> instances = habitInstanceService.getHabitInstancesByUserId(userDetails.getId());
        return ResponseEntity.ok(habitInstanceMapper.toHabitInstanceDTOList(instances));
    }

    @GetMapping("/active")
    public ResponseEntity<List<HabitInstanceDto>> getActiveUserInstances(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<HabitInstance> instances = habitInstanceService.getActiveHabitInstancesByUserId(userDetails.getId());
        return ResponseEntity.ok(habitInstanceMapper.toHabitInstanceDTOList(instances));
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<HabitInstanceDto>> getInstancesByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<HabitInstance> instances = habitInstanceService.getHabitInstancesByUserIdAndDate(userDetails.getId(), date);
        return ResponseEntity.ok(habitInstanceMapper.toHabitInstanceDTOList(instances));
    }

    @GetMapping("/range")
    public ResponseEntity<List<HabitInstanceDto>> getInstancesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<HabitInstance> instances = habitInstanceService.getHabitInstancesByUserIdAndDateRange(
                userDetails.getId(), startDate, endDate);
        return ResponseEntity.ok(habitInstanceMapper.toHabitInstanceDTOList(instances));
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<HabitInstanceDto> completeInstance(@PathVariable Long id) {
        try {
            HabitInstance instance = habitInstanceService.completeHabitInstance(id);
            return ResponseEntity.ok(habitInstanceMapper.toHabitInstanceDTO(instance));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/habit/{habitId}/generate")
    public ResponseEntity<Void> generateInstances(@PathVariable Long habitId) {
        try {
            habitInstanceService.generateHabitInstances(habitId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
