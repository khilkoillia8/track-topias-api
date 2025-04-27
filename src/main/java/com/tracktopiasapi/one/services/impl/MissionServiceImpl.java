package com.tracktopiasapi.one.services.impl;

import com.tracktopiasapi.one.config.UserDetailsImpl;
import com.tracktopiasapi.one.model.Mission;
import com.tracktopiasapi.one.model.User;
import com.tracktopiasapi.one.repository.MissionRepository;
import com.tracktopiasapi.one.repository.UserRepository;
import com.tracktopiasapi.one.services.LevelService;
import com.tracktopiasapi.one.services.MissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MissionServiceImpl implements MissionService {

    private final MissionRepository missionRepository;
    private final UserRepository userRepository;
    private final LevelService levelService;

    @Override
    public List<Mission> getAllMissions() {
        return missionRepository.findAll();
    }

    @Override
    public Optional<Mission> getMissionById(Long id) {
        return missionRepository.findById(id);
    }

    @Override
    @Transactional
    public Mission createMission(Mission mission, UserDetailsImpl userDetails) {
        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        mission.setUser(user);
        return missionRepository.save(mission);
    }

    @Override
    @Transactional
    public Optional<Mission> updateMission(Long id, Mission missionDetails) {
        return missionRepository.findById(id)
                .map(existingMission -> {
                    boolean wasCompleted = existingMission.isCompleted();
                    boolean isCompleted = missionDetails.isCompleted();
                    
                    existingMission.setTitle(missionDetails.getTitle());
                    existingMission.setDescription(missionDetails.getDescription());
                    existingMission.setDueDate(missionDetails.getDueDate());
                    existingMission.setPriority(missionDetails.getPriority());
                    existingMission.setCompleted(isCompleted);
                    
                    if (wasCompleted != isCompleted) {
                        User user = existingMission.getUser();
                        if (isCompleted) {
                            levelService.addPoints(user, LevelServiceImpl.MISSION_COMPLETION_POINTS);
                        } else {
                            levelService.removePoints(user, LevelServiceImpl.MISSION_COMPLETION_POINTS);
                        }
                    }
                    
                    return missionRepository.save(existingMission);
                });
    }

    @Override
    @Transactional
    public boolean deleteMission(Long id) {
        return missionRepository.findById(id)
                .map(mission -> {
                    if (mission.isCompleted()) {
                        levelService.removePoints(mission.getUser(), LevelServiceImpl.MISSION_COMPLETION_POINTS);
                    }
                    missionRepository.delete(mission);
                    return true;
                })
                .orElse(false);
    }

    @Override
    public List<Mission> getAllMissionsByUserId(Long id) {
        return missionRepository.findAllByUserId(id);
    }
}
