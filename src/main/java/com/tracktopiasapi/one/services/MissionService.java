package com.tracktopiasapi.one.services;

import com.tracktopiasapi.one.config.UserDetailsImpl;
import com.tracktopiasapi.one.model.Mission;

import java.util.List;
import java.util.Optional;

public interface MissionService {
    
    List<Mission> getAllMissions();
    
    Optional<Mission> getMissionById(Long id);
    
    Mission createMission(Mission mission, UserDetailsImpl userDetails);
    
    Optional<Mission> updateMission(Long id, Mission missionDetails);
    
    boolean deleteMission(Long id);

    List<Mission> getAllMissionsByUserId(Long id);
}
