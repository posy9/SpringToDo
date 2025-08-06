package com.emobile.springtodo.dto.mapper;

import com.emobile.springtodo.dto.input.UserRequest;
import com.emobile.springtodo.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserRequestMapper extends BaseMapper<User, UserRequest> {
    @Override
    @Mapping(source = "username", target = "username")
    UserRequest toDto(User user);

    @Override
    @Mapping(source = "username", target = "username")
    User toEntity(UserRequest userRequest);
}
