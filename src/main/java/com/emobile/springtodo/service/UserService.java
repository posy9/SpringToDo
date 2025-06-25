package com.emobile.springtodo.service;

import com.emobile.springtodo.entity.User;
import com.emobile.springtodo.exception.EntityExistsException;
import com.emobile.springtodo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService extends AbstractService<User> {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        super(userRepository);
        this.userRepository = userRepository;
    }

    @Transactional
    public void save(User user) {
        if(userRepository.findByUsername(user.getUsername()) == null) {
            super.save(user);
        }
        else {
            throw new EntityExistsException(String.format("User %s already exists", user.getUsername()));
        }
    }
}
