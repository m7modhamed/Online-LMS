package com.lms.onlinelms.usermanagement.mapper;

import com.lms.onlinelms.usermanagement.dto.UserDto;
import com.lms.onlinelms.usermanagement.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IUserMapper {
    UserDto toUserDto(User user);
}
