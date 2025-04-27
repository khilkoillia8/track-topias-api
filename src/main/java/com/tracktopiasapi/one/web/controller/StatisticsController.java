package com.tracktopiasapi.one.web.controller;

import com.tracktopiasapi.one.config.UserDetailsImpl;
import com.tracktopiasapi.one.services.StatisticsService;
import com.tracktopiasapi.one.web.dto.statistics.CompletedHabitsByTopicDto;
import com.tracktopiasapi.one.web.dto.statistics.CompletedMissionsByTopicDto;
import com.tracktopiasapi.one.web.dto.statistics.UserStatisticsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping
    public ResponseEntity<UserStatisticsDto> getUserStatistics(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(statisticsService.getUserStatistics(userDetails.getId()));
    }
}
