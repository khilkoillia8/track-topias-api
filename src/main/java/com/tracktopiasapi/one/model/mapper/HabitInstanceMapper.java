package com.tracktopiasapi.one.model.mapper;

import com.tracktopiasapi.one.model.HabitInstance;
import com.tracktopiasapi.one.web.dto.HabitInstanceDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface HabitInstanceMapper {
    
    @Mapping(target = "habitId", source = "habit.id")
    @Mapping(target = "habitTitle", source = "habit.title")
    HabitInstanceDto toHabitInstanceDTO(HabitInstance habitInstance);
    
    List<HabitInstanceDto> toHabitInstanceDTOList(List<HabitInstance> habitInstances);
}
