package com.emobile.springtodo.repository;

import com.emobile.springtodo.entity.Task;
import com.emobile.springtodo.exception.EntityNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TaskRepository extends AbstractRepository<Task> {

    public TaskRepository(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public List<Task> findAll(int limit, int offset) {
        String jpql = "SELECT t FROM Task t";
        TypedQuery<Task> query = entityManager.createQuery(jpql, Task.class);
        query.setFirstResult(offset);
        query.setMaxResults(limit);
        List<Task> tasks = query.getResultList();
        if (tasks.isEmpty()) {
            throw new EntityNotFoundException("Tasks for your request are not found");
        }
        return tasks;
    }

    public List<Task> findAllForUser(Long userId, int limit, int offset) {
        String jpql = "SELECT t FROM Task t WHERE t.user.id = :userId";
        TypedQuery<Task> query = entityManager.createQuery(jpql, Task.class);
        query.setParameter("userId", userId);
        query.setFirstResult(offset);
        query.setMaxResults(limit);
        List<Task> tasks = query.getResultList();
        if (tasks.isEmpty()) {
            throw new EntityNotFoundException("Tasks for your request are not found");
        }
        return tasks;
    }
}
