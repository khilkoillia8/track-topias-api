package com.tracktopiasapi.one.web.dto;
import lombok.Data;
import java.util.Date;

@Data
public class MissionDto {
    private Long id;
    private String title;
    private String description;
    private Date dueDate;
    private boolean completed;
    private String priority;
}
