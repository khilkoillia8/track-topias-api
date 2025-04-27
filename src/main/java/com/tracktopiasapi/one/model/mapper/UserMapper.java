package com.tracktopiasapi.one.model.mapper;
import java.util.List;

import com.tracktopiasapi.one.model.User;
import com.tracktopiasapi.one.web.dto.UserCreationDto;
import com.tracktopiasapi.one.web.dto.UserDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", uses = {LevelMapper.class})
public interface UserMapper {
    UserDto toUserDTO(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User toUser(UserCreationDto userDto);

    List<UserDto> toUserDTOList(List<User> allUsers);
}