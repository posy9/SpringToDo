package com.emobile.springtodo.repository.mapper;

import com.emobile.springtodo.entity.Task;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper extends BeanPropertyRowMapper<Task> {
    public TaskMapper() {
        super(Task.class);
    }
}
