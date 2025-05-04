package com.tracktopiasapi.one.web.controller;

import com.tracktopiasapi.one.config.UserDetailsImpl;
import com.tracktopiasapi.one.services.StatisticsService;
import com.tracktopiasapi.one.web.dto.statistics.CompletedHabitsByTopicDto;
import com.tracktopiasapi.one.web.dto.statistics.CompletedMissionsByTopicDto;
import com.tracktopiasapi.one.web.dto.statistics.HabitInstanceStatisticsDto;
import com.tracktopiasapi.one.web.dto.statistics.UserHabitInstanceStatisticsDto;
import com.tracktopiasapi.one.web.dto.statistics.UserRatingDto;
import com.tracktopiasapi.one.web.dto.statistics.UserStatisticsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/statistics")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping("/habits")
    public ResponseEntity<List<CompletedHabitsByTopicDto>> getHabitStatistics(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(statisticsService.getCompletedHabitsByTopic(userDetails.getId()));
    }

    @GetMapping("/missions")
    public ResponseEntity<List<CompletedMissionsByTopicDto>> getMissionStatistics(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(statisticsService.getCompletedMissionsByTopic(userDetails.getId()));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserStatisticsDto> getUserStatistics(@PathVariable Long id) {
        return ResponseEntity.ok(statisticsService.getUserStatistics(id));
    }

    @GetMapping
    public ResponseEntity<UserStatisticsDto> getUserStatistics(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        System.out.println(2);
        return ResponseEntity.ok(statisticsService.getUserStatistics(userDetails.getId()));
    }

    @GetMapping("/habit-instances")
    public ResponseEntity<UserHabitInstanceStatisticsDto> getHabitInstanceStatistics(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(statisticsService.getUserHabitInstanceStatistics(userDetails.getId()));
    }
    
    @GetMapping("/habit-instances/by-habit")
    public ResponseEntity<List<HabitInstanceStatisticsDto>> getHabitInstanceStatisticsByHabit(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(statisticsService.getHabitInstanceStatisticsByHabit(userDetails.getId()));
    }
    
    @GetMapping("/habit-instances/habit/{habitId}")
    public ResponseEntity<HabitInstanceStatisticsDto> getHabitInstanceStatisticsForHabit(
            @PathVariable Long habitId) {
        return ResponseEntity.ok(statisticsService.getHabitInstanceStatisticsForHabit(habitId));
    }
    
    @GetMapping("/ratings/level")
    public ResponseEntity<List<UserRatingDto>> getUserRatingsByLevel() {
        return ResponseEntity.ok(statisticsService.getUserRatingsByLevel());
    }
    
    @GetMapping("/ratings/missions")
    public ResponseEntity<List<UserRatingDto>> getUserRatingsByMissionsCompleted() {
        return ResponseEntity.ok(statisticsService.getUserRatingsByMissionsCompleted());
    }
    
    @GetMapping("/ratings/habit-streak")
    public ResponseEntity<List<UserRatingDto>> getUserRatingsByMaxHabitStreak() {
        return ResponseEntity.ok(statisticsService.getUserRatingsByMaxHabitStreak());
    }
    
    @GetMapping("/ratings/habit-completions")
    public ResponseEntity<List<UserRatingDto>> getUserRatingsByHabitCompletions() {
        return ResponseEntity.ok(statisticsService.getUserRatingsByHabitCompletions());
    }
}
