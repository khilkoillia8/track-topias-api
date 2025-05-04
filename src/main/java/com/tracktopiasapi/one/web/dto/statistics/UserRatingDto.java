package com.tracktopiasapi.one.web.dto.statistics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRatingDto {
    private Long userId;
    private String username;
    private Integer ranking;
    private Integer value;
}
