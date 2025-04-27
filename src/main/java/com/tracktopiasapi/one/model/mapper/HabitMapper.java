package com.tracktopiasapi.one.model.mapper;

import java.util.List;

import com.tracktopiasapi.one.model.Habit;
import com.tracktopiasapi.one.web.dto.HabitCreationDto;
import com.tracktopiasapi.one.web.dto.HabitDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", uses = {TopicMapper.class})
public interface HabitMapper {
    
    HabitDto toHabitDTO(Habit habit);
    
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "topics", ignore = true)
    Habit toHabit(HabitCreationDto habitDto);
    
    List<HabitDto> toHabitDTOList(List<Habit> habits);
}
