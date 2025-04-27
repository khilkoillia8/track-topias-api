package com.tracktopiasapi.one.repository;

import com.tracktopiasapi.one.model.Level;
import com.tracktopiasapi.one.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<Object> findByEmail(String email);
}