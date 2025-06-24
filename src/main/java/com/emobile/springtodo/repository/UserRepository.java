package com.emobile.springtodo.repository;

import com.emobile.springtodo.entity.User;
import com.emobile.springtodo.repository.mapper.UserMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import static com.emobile.springtodo.repository.util.Tables.USERS;

@Repository
public class UserRepository extends AbstractRepository<User> {
    public UserRepository(UserMapper rowMapper, JdbcTemplate jdbcTemplate) {
        super(rowMapper, jdbcTemplate, USERS);
    }
}
