package com.emobile.springtodo.service;

import com.emobile.springtodo.entity.Task;
import com.emobile.springtodo.exception.EntityNotFoundException;
import com.emobile.springtodo.repository.TaskRepository;
import com.emobile.springtodo.repository.UserRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class TaskService extends AbstractService<Task> {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository entityRepository, UserRepository userRepository) {
        super(entityRepository);
        this.taskRepository = entityRepository;
        this.userRepository = userRepository;

    }

    @Override
    @Cacheable(
            cacheNames = "tasksList",
            key = "#page + '_' + #size"
    )
    public List<Task> findAll(int page, int size) {
        return super.findAll(page, size);
    }


    @Cacheable(
            cacheNames = "tasksForUser",
            key = "#userId + '_' + #page + '_' + #size"
    )
    public List<Task> findAllForUser(long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return taskRepository.findByUserId(userId, pageable).getContent();
    }

    @Override
    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(cacheNames = "tasksForUser", allEntries = true),
                    @CacheEvict(cacheNames = "tasksList", allEntries = true)
            }
    )
    public void save(Task task) {
        if (task.getUser().getId() != null && userRepository.existsById(task.getUser().getId())) {
            super.save(task);
        } else {
            throw new EntityNotFoundException(String.format("User with id %s not found", task.getUser().getId()));
        }
    }

    @Override
    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(cacheNames = "tasks", key = "#task.id"),
                    @CacheEvict(cacheNames = "tasksForUser", allEntries = true),
                    @CacheEvict(cacheNames = "tasksList", allEntries = true)
            }
    )
    public void update(Task task) {
        if (task.getUser().getId() == null || userRepository.existsById(task.getUser().getId())) {
            super.update(task);
        } else {
            throw new EntityNotFoundException(String.format("User with id %s not found", task.getUser().getId()));
        }
    }

    @Override
    @Cacheable(value = "tasks", key = "#id")
    public Task findById(long id) {
        return super.findById(id);
    }


    @Caching(
            evict = {
                    @CacheEvict(cacheNames = "tasks", key = "#id"),
                    @CacheEvict(cacheNames = "tasksForUser", allEntries = true),
                    @CacheEvict(cacheNames = "tasksList", allEntries = true)
            }
    )
    @Override
    public void delete(long id) {
        if (taskRepository.existsById(id)) {
            super.delete(id);
        } else {
            throw new EntityNotFoundException(String.format("Task with id %s not found", id));
        }

    }

}
