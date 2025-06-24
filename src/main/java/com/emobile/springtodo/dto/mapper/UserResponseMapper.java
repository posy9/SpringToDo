package com.emobile.springtodo.dto.mapper;

import com.emobile.springtodo.dto.output.UserResponse;
import com.emobile.springtodo.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserResponseMapper extends BaseMapper<User, UserResponse> {
    @Override
    @Mapping(source = "username", target = "username")
    @Mapping(source = "id", target = "id")
    UserResponse toDto(User user);

    @Override
    @Mapping(source = "username", target = "username")
    @Mapping(source = "id", target = "id")
    User toEntity(UserResponse userResponse);
}
