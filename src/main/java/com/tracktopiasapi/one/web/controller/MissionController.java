package com.tracktopiasapi.one.web.controller;

import com.tracktopiasapi.one.config.UserDetailsImpl;
import com.tracktopiasapi.one.model.Mission;
import com.tracktopiasapi.one.model.User;
import com.tracktopiasapi.one.services.MissionService;
import com.tracktopiasapi.one.web.dto.MissionCreationDto;
import com.tracktopiasapi.one.web.dto.MissionDto;
import com.tracktopiasapi.one.model.mapper.MissionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/missions")
@RequiredArgsConstructor
public class MissionController {
    private final MissionService missionService;
    private final MissionMapper missionMapper;

    @GetMapping
    public ResponseEntity<List<MissionDto>> getAllMissionsByUserId(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        var missions = missionService.getAllMissionsByUserId(userDetails.getId());
        return ResponseEntity.ok(missionMapper.toMissionDTOList(missions));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MissionDto> getMissionById(@PathVariable Long id) {
        return ResponseEntity.of(missionService.getMissionById(id).map(missionMapper::toMissionDTO));
    }

    @PostMapping
    public ResponseEntity<MissionDto> createMission(
            @RequestBody MissionCreationDto missionDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Mission mission = missionMapper.toMission(missionDto);
        Mission createdMission = missionService.createMission(mission, userDetails);
        return ResponseEntity.ok(missionMapper.toMissionDTO(createdMission));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MissionDto> updateMission(
            @PathVariable Long id,
            @RequestBody MissionCreationDto missionDto) {
        Mission missionDetails = missionMapper.toMission(missionDto);
        return ResponseEntity.of(
                missionService.updateMission(id, missionDetails).map(missionMapper::toMissionDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMission(@PathVariable Long id) {
        if (missionService.deleteMission(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
