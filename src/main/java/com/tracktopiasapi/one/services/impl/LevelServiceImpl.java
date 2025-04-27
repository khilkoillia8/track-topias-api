package com.tracktopiasapi.one.services.impl;

import com.tracktopiasapi.one.model.Level;
import com.tracktopiasapi.one.model.User;
import com.tracktopiasapi.one.repository.LevelRepository;
import com.tracktopiasapi.one.repository.UserRepository;
import com.tracktopiasapi.one.services.LevelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LevelServiceImpl implements LevelService {

    private final LevelRepository levelRepository;
    private final UserRepository userRepository;
    
    public static final int HABIT_COMPLETION_POINTS = 10;
    public static final int MISSION_COMPLETION_POINTS = 20;

    @Override
    @Transactional
    public void addPoints(User user, int points) {
        if (user.getLevel() == null) {
            initializeLevel(user);
        }
        
        Level level = user.getLevel();
        level.addScore(points);
        levelRepository.save(level);
    }

    @Override
    @Transactional
    public void removePoints(User user, int points) {
        if (user.getLevel() == null) {
            return;
        }
        
        Level level = user.getLevel();
        level.removeScore(points);
        levelRepository.save(level);
    }

    @Override
    public Optional<Level> getLevelById(Long id) {
        return levelRepository.findById(id);
    }

    @Override
    @Transactional
    public Level initializeLevel(User user) {
        Level level = new Level();
        level.setLevel(1);
        level.setScore(0);
        level.setScoreToNextLevel(100);
        level = levelRepository.save(level);
        
        user.setLevel(level);
        userRepository.save(user);
        
        return level;
    }
}
