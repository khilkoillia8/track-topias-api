package com.tracktopiasapi.one.services;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.tracktopiasapi.one.model.Level;
import com.tracktopiasapi.one.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> findById(Long id);

    Optional<DecodedJWT> signIn(String username, String password);

    User signUp(User user);

    List<User> getAllUsers();

    Optional<User> getById(Long id);

    Optional<Level> getLevelByUserId(Long id);
}
