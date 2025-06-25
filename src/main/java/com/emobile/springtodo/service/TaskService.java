package com.emobile.springtodo.service;

import com.emobile.springtodo.entity.Task;
import com.emobile.springtodo.exception.EntityNotFoundException;
import com.emobile.springtodo.repository.TaskRepository;
import com.emobile.springtodo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TaskService extends AbstractService<Task> {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository entityRepository, UserRepository userRepository) {
        super(entityRepository);
        this.taskRepository = entityRepository;
        this.userRepository = userRepository;

    }

    @Transactional(readOnly = true)
    public List<Task> findAllForUser(long id, int page, int size) {
        int offset = page * size;
        return taskRepository.findAllForUser(id, size, offset);
    }

    @Override
    @Transactional
    public void save(Task task) {
        if(task.getUserId() != null && userRepository.existsById(task.getUserId())) {
            super.save(task);
        } else {
            throw new EntityNotFoundException(String.format("User with id %s not found", task.getUserId()));
        }
    }

    @Override
    @Transactional
    public void update(Task task) {
        if(task.getUserId() == null || userRepository.existsById(task.getUserId())) {
            super.update(task);
        } else {
            throw new EntityNotFoundException(String.format("User with id %s not found", task.getUserId()));
        }
    }

}
