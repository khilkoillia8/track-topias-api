package com.tracktopiasapi.one.services;

import com.tracktopiasapi.one.config.UserDetailsImpl;
import com.tracktopiasapi.one.model.Mission;
import com.tracktopiasapi.one.web.dto.MissionCreationDto;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface MissionService {
    
    List<Mission> getAllMissions();
    
    Optional<Mission> getMissionById(Long id);
    
    Mission createMission(Mission mission, MissionCreationDto missionDto, UserDetailsImpl userDetails);
    
    Optional<Mission> updateMission(Long id, Mission missionDetails, Set<Long> topicIds);
    
    boolean deleteMission(Long id);

    List<Mission> getAllMissionsByUserId(Long id);
}
