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
public class UserStatisticsDto {
    private Long userId;
    private String username;
    private Long totalHabits;
    private Long completedHabits;
    private Long totalMissions;
    private Long completedMissions;
    private List<CompletedHabitsByTopicDto> habitsByTopic;
    private List<CompletedMissionsByTopicDto> missionsByTopic;
}
