package com.emobile.springtodo.repository;

import com.emobile.springtodo.entity.Status;
import com.emobile.springtodo.entity.Task;
import com.emobile.springtodo.exception.EntityNotFoundException;
import com.emobile.springtodo.repository.mapper.TaskMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.emobile.springtodo.repository.util.Tables.TASKS;

@Repository
public class TaskRepository extends AbstractRepository<Task> {

    public TaskRepository(TaskMapper rowMapper, JdbcTemplate jdbcTemplate) {
        super(rowMapper, jdbcTemplate, TASKS);
    }

    public List<Task> findAllForUser(Long userId, int limit, int offset) {
        String sql = String.format("SELECT * FROM %s WHERE userId=? LIMIT ? OFFSET ?", TASKS);
        var foundTasks = jdbcTemplate.query(sql, rowMapper, userId, limit, offset);
        if (foundTasks.isEmpty()) {
            throw new EntityNotFoundException("Tasks for your request are not found");
        }
        return foundTasks;
    }

    @Override
    @Transactional
    public void save(Task task) {
        if (task.getStatus() == null) {
            task.setStatus(Status.CREATED);
        }
        super.save(task);
    }
}
