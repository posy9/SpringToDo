package com.emobile.springtodo.dto.mapper;

import com.emobile.springtodo.dto.input.TaskRequest;
import com.emobile.springtodo.entity.Task;
import com.emobile.springtodo.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface TaskRequestMapper extends BaseMapper<Task, TaskRequest> {
    @Override
    @Mapping(source = "title", target = "title")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "user", target = "userId", qualifiedByName = "userToUserId")
    TaskRequest toDto(Task task);

    @Override
    @Mapping(source = "title", target = "title")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "userId", target = "user", qualifiedByName = "userIdToUser")
    Task toEntity(TaskRequest taskRequest);

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
