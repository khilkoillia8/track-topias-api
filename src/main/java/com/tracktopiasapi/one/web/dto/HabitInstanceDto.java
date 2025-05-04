package com.tracktopiasapi.one.web.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class HabitInstanceDto {
    private Long id;
    private Long habitId;
    private String habitTitle;
    private LocalDate date;
    private String weekday;
    private boolean completed;
    private boolean active;
}
