package com.tracktopiasapi.one.model.mapper;

import java.util.List;

import com.tracktopiasapi.one.model.Mission;
import com.tracktopiasapi.one.web.dto.MissionCreationDto;
import com.tracktopiasapi.one.web.dto.MissionDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", uses = {TopicMapper.class})
public interface MissionMapper {
    
    MissionDto toMissionDTO(Mission mission);
    
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "user", ignore = true) 
    @Mapping(target = "topics", ignore = true)
    Mission toMission(MissionCreationDto missionDto);
    
    List<MissionDto> toMissionDTOList(List<Mission> missions);
}
