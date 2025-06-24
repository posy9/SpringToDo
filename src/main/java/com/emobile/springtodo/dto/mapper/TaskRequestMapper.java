package com.emobile.springtodo.dto.mapper;

import com.emobile.springtodo.dto.input.TaskRequest;
import com.emobile.springtodo.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TaskRequestMapper extends BaseMapper<Task, TaskRequest> {
    @Override
    @Mapping(source = "title", target = "title")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "userId", target = "userId")
    TaskRequest toDto(Task task);

    @Override
    @Mapping(source = "title", target = "title")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "userId", target = "userId")
    Task toEntity(TaskRequest taskRequest);

}
