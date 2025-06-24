package com.emobile.springtodo.dto.mapper;

import com.emobile.springtodo.dto.output.TaskResponse;
import com.emobile.springtodo.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TaskResponseMapper extends BaseMapper<Task, TaskResponse> {
    @Override
    @Mapping(source = "id", target = "id")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "userId", target = "userId")
    TaskResponse toDto(Task task);

    @Override
    @Mapping(source = "id", target = "id")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "userId", target = "userId")
    Task toEntity(TaskResponse taskResponse);

}
