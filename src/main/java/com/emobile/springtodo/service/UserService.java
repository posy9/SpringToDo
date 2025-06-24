package com.emobile.springtodo.service;

import com.emobile.springtodo.entity.User;
import com.emobile.springtodo.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService extends AbstractService<User> {

    public UserService(UserRepository userRepository) {
        super(userRepository);
    }
}
