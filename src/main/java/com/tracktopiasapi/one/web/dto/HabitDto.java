package com.tracktopiasapi.one.web.dto;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class HabitDto {
    private Long id;
    private String title;
    private String description;
    private List<String> weekdays;
    private boolean completed;
    private int currentStreak;
    private int bestStreak;
    private boolean streakBroken;
    private Set<TopicDto> topics;
}
