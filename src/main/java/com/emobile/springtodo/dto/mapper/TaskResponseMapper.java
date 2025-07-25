package com.emobile.springtodo.dto.mapper;

import com.emobile.springtodo.dto.output.TaskResponse;
import com.emobile.springtodo.entity.Task;
import com.emobile.springtodo.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface TaskResponseMapper extends BaseMapper<Task, TaskResponse> {
    @Override
    @Mapping(source = "id", target = "id")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "user", target = "userId", qualifiedByName = "userToUserId")
    TaskResponse toDto(Task task);

    @Override
    @Mapping(source = "id", target = "id")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "userId", target = "user", qualifiedByName = "userIdToUser")
    Task toEntity(TaskResponse taskResponse);

    @Named("userIdToUser")
    default User userIdToUser(Long userId) {
        if (userId == null) {
            return null;
        }
        var user = new User();
        user.setId(userId);
        return user;
    }

    @Named("userToUserId")
    default Long userToUserId(User user) {
        return user.getId();
    }

}
