package com.tracktopiasapi.one.model.mapper;

import com.tracktopiasapi.one.model.Topic;
import com.tracktopiasapi.one.web.dto.TopicCreationDto;
import com.tracktopiasapi.one.web.dto.TopicDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface TopicMapper {
    
    TopicDto toTopicDTO(Topic topic);
    
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "missions", ignore = true)
    @Mapping(target = "habits", ignore = true)
    Topic toTopic(TopicCreationDto topicDto);
    
    List<TopicDto> toTopicDTOList(List<Topic> topics);
    
    List<TopicDto> toTopicDTOList(Set<Topic> topics);
}
