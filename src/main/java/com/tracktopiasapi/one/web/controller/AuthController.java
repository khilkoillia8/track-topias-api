package com.tracktopiasapi.one.web.controller;

import com.tracktopiasapi.one.model.mapper.JWTTokenMapper;
import com.tracktopiasapi.one.model.mapper.UserMapper;
import com.tracktopiasapi.one.services.UserService;
import com.tracktopiasapi.one.web.controller.auth.JWTToken;
import com.tracktopiasapi.one.web.dto.UserCreationDto;
import com.tracktopiasapi.one.web.dto.UserDto;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final UserMapper userMapper;
    private final JWTTokenMapper jwtTokenMapper;

    @PostMapping("/sign-up")
    public ResponseEntity<UserDto> signUp(@RequestBody @Valid UserCreationDto userDto) {
        var newUser = userService.signUp(userMapper.toUser(userDto));
        return new ResponseEntity<>(userMapper.toUserDTO(newUser), HttpStatus.CREATED);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<JWTToken> signIn(@RequestBody @Valid UserCreationDto userDto) {
        return ResponseEntity.of(
                userService
                        .signIn(userDto.getUsername(), userDto.getPassword())
                        .map(jwtTokenMapper::toPayload));
    }
}