package com.tracktopiasapi.one.repository;

import com.tracktopiasapi.one.model.Mission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MissionRepository extends JpaRepository<Mission, Long> {
    List<Mission> findAllByUserId(Long userId);
}
