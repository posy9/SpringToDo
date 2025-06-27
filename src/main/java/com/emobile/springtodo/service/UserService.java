package com.emobile.springtodo.service;

import com.emobile.springtodo.entity.User;
import com.emobile.springtodo.exception.EntityExistsException;
import com.emobile.springtodo.exception.EntityNotFoundException;
import com.emobile.springtodo.repository.UserRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class UserService extends AbstractService<User> {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        super(userRepository);
        this.userRepository = userRepository;
    }

    @Transactional
    @CacheEvict(value = "usersList", allEntries = true)
    public void save(User user) {
        if (userRepository.findByUsername(user.getUsername()) == null) {
            super.save(user);
        } else {
            throw new EntityExistsException(String.format("User %s already exists", user.getUsername()));
        }
    }

    @Override
    @Cacheable(value = "users", key = "#id")
    public User findById(long id) {
        return super.findById(id);
    }

    @Override
    @Cacheable(value = "usersList", key = "#page + '_' + #size")
    public List<User> findAll(int page, int size) {
        return super.findAll(page, size);
    }

    @Override
    @Caching(
            evict = {
                    @CacheEvict(cacheNames = "users", key = "#id"),
                    @CacheEvict(cacheNames = "usersList", allEntries = true),
                    @CacheEvict(cacheNames = "tasksForUser", allEntries = true)
            }
    )
    public void delete(long id) {
        if (userRepository.existsById(id)) {
            super.delete(id);
        } else {
            throw new EntityNotFoundException(String.format("User with id %s not found", id));
        }
    }

    @Override
    @Caching(
            evict = {
                    @CacheEvict(cacheNames = "users", key = "#user.id"),
                    @CacheEvict(cacheNames = "usersList", allEntries = true)
            }
    )
    public void update(User user) {
        if (userRepository.findByUsername(user.getUsername()) == null) {
            super.update(user);
        } else {
            throw new EntityExistsException(String.format("User %s already exists", user.getUsername()));
        }
    }


}
