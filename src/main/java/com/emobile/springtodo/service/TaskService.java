package com.emobile.springtodo.service;

import com.emobile.springtodo.entity.Task;
import com.emobile.springtodo.repository.AbstractRepository;
import com.emobile.springtodo.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TaskService extends AbstractService<Task> {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository entityRepository) {
        super(entityRepository);
        this.taskRepository = entityRepository;
    }

    @Transactional(readOnly = true)
    public List<Task> findAllForUser(long id, int page, int size) {
        return taskRepository.findAllForUser(id, page, size);
    }

}
