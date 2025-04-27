package com.tracktopiasapi.one.web.controller;

import com.tracktopiasapi.one.model.Level;
import com.tracktopiasapi.one.model.mapper.LevelMapper;
import com.tracktopiasapi.one.web.dto.LevelDto;
import com.tracktopiasapi.one.web.dto.UserDto;
import com.tracktopiasapi.one.model.mapper.UserMapper;
import com.tracktopiasapi.one.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;
    private final LevelMapper levelMapper;

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> findById (@PathVariable Long id){
        return ResponseEntity.of(userService.getById(id).map(userMapper::toUserDTO));
    }
    @GetMapping("/{id}/level")
    public ResponseEntity<LevelDto> getLevelByUserId (@PathVariable Long id){
        return ResponseEntity.of(userService.getLevelByUserId(id).map(levelMapper::toLevelDTO));
    }
    @GetMapping()
    public ResponseEntity<List<UserDto>> allUsers() {
        return new ResponseEntity<>(userMapper.toUserDTOList(userService.getAllUsers()), HttpStatus.OK);
    }
}
