package com.emobile.springtodo.repository;

import com.emobile.springtodo.entity.Task;
import com.emobile.springtodo.repository.mapper.TaskMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.emobile.springtodo.repository.util.Tables.TASKS;

@Repository
public class TaskRepository extends AbstractRepository<Task> {

    public TaskRepository(TaskMapper rowMapper, JdbcTemplate jdbcTemplate) {
        super(rowMapper, jdbcTemplate, TASKS);
    }

    public List<Task> findAllForUser(Long userId, int limit, int offset) {
        String sql = "SELECT * FROM ? WHERE userId=? LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, rowMapper, tableName, userId, limit, offset);
    }
}
