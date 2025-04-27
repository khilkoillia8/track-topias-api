package com.tracktopiasapi.one.services.impl;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.tracktopiasapi.one.config.JWTTokenProvider;
import com.tracktopiasapi.one.exeption.InvalidPasswordException;
import com.tracktopiasapi.one.exeption.UserAlreadyExistsException;
import com.tracktopiasapi.one.exeption.UserNotFoundException;
import com.tracktopiasapi.one.model.Level;
import com.tracktopiasapi.one.model.User;
import com.tracktopiasapi.one.repository.UserRepository;
import com.tracktopiasapi.one.services.LevelService;
import com.tracktopiasapi.one.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final JWTTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final LevelService levelService;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<Level> getLevelByUserId(Long id) {
        return Optional.of(getById(id).get().getLevel());
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<DecodedJWT> signIn(String username, String password) {
        var user =
                userRepository
                        .findByUsername(username)
                        .orElseThrow(
                                () ->
                                        new UserNotFoundException(
                                                "User with username %s not found".formatted(username)));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidPasswordException("Invalid password");
        }
        return jwtTokenProvider.toDecodedJWT(
                jwtTokenProvider.generateToken(user.getId(), username, user.getRole()));
    }

    @Override
    @Transactional
    public User signUp(User userData) {
        existsByUsername(userData);
        existsByEmail(userData);
        userData.setRole("ROLE_ADMIN");
        userData.setPassword(passwordEncoder.encode(userData.getPassword()));
        User savedUser = userRepository.save(userData);

        levelService.initializeLevel(savedUser);
        
        return savedUser;
    }

    private void existsByUsername(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new UserAlreadyExistsException(
                    "Username %s is already in use".formatted(user.getUsername()));
        }
    }

    private void existsByEmail(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistsException(
                    "Email %s is already in use".formatted(user.getEmail()));
        }
    }
}
