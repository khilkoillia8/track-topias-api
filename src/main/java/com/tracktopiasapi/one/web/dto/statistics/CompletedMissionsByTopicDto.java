package com.tracktopiasapi.one.web.dto.statistics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompletedMissionsByTopicDto {
    private Long topicId;
    private String topicName;
    private Long completedCount;
}
