package com.emobile.springtodo.repository;

import com.emobile.springtodo.entity.User;
import com.emobile.springtodo.exception.EntityNotFoundException;
import com.emobile.springtodo.repository.mapper.UserMapper;
import org.postgresql.util.PSQLException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import static com.emobile.springtodo.repository.util.Tables.TASKS;
import static com.emobile.springtodo.repository.util.Tables.USERS;

@Repository
public class UserRepository extends AbstractRepository<User> {
    public UserRepository(UserMapper rowMapper, JdbcTemplate jdbcTemplate) {
        super(rowMapper, jdbcTemplate, USERS);
    }


    public User findByUsername(String username) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM users WHERE username = ?", rowMapper, username);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public boolean existsById(Long id) {
        try {
            super.findById(id);
            return true;
        } catch (EntityNotFoundException e) {
            return false;
        }
    }

}
