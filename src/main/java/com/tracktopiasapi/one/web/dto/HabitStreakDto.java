package com.tracktopiasapi.one.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HabitStreakDto {
    private Long habitId;
    private int currentStreak;
    private int bestStreak;
    private boolean streakBroken;
}
