package com.tracktopiasapi.one.web.dto.statistics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HabitInstanceStatisticsDto {
    private Long habitId;
    private String habitTitle;
    private Long totalInstances;
    private Long completedInstances;
    private Double completionRate;
    private Integer currentStreak;
    private Integer bestStreak;
}
