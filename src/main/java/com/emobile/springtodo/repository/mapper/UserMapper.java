package com.emobile.springtodo.repository.mapper;

import com.emobile.springtodo.entity.User;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Component;

@Component
public class UserMapper extends BeanPropertyRowMapper<User> {
    public UserMapper() {
        super(User.class); // Явно указываем класс для маппинга
    }
}
