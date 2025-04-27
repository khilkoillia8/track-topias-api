package com.tracktopiasapi.one.model.mapper;

import com.tracktopiasapi.one.model.Level;
import com.tracktopiasapi.one.web.dto.LevelDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LevelMapper {
    LevelDto toLevelDTO(Level level);
    Level toLevel(LevelDto levelDto);
}
