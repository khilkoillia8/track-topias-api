package com.tracktopiasapi.one.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TopicCreationDto {
    
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String name;
    
    @NotBlank(message = "Color is required")
    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$", message = "Color must be in hex format (e.g., #000000)")
    private String color;
}
