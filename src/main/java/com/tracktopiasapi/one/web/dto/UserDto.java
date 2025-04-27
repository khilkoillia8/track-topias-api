package com.tracktopiasapi.one.web.dto;
import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private String role;
    private LevelDto level;
}