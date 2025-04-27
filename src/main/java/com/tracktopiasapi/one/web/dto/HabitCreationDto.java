package com.tracktopiasapi.one.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class HabitCreationDto {
    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    private String title;
    
    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;
    
    @NotEmpty(message = "Weekdays is required")
    private List<String> weekdays;
    
    private boolean completed = false;
    
    @NotEmpty(message = "At least one topic is required")
    private Set<Long> topicIds;
}
