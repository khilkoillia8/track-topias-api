package com.tracktopiasapi.one.web.dto;
import lombok.Data;

@Data
public class LevelDto {
    private Long id;
    private int level;
    private int score;
    private int scoreToNextLevel;
}
