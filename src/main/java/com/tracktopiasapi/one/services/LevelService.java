package com.tracktopiasapi.one.services;

import com.tracktopiasapi.one.model.Level;
import com.tracktopiasapi.one.model.User;

import java.util.Optional;

public interface LevelService {
    void addPoints(User user, int points);
    
    void removePoints(User user, int points);

    Optional<Level> getLevelById(Long id);
    
    Level initializeLevel(User user);
}
