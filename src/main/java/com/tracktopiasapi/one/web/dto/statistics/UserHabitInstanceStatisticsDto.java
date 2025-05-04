package com.tracktopiasapi.one.web.dto.statistics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserHabitInstanceStatisticsDto {
    private Long userId;
    private String username;
    private Long totalHabitInstances;
    private Long completedHabitInstances;
    private Double overallCompletionRate;
    private List<HabitInstanceStatisticsDto> habitInstancesByHabit;
}
