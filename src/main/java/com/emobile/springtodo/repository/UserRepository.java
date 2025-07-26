package com.emobile.springtodo.repository;

import com.emobile.springtodo.entity.User;

import java.util.Optional;

public interface UserRepository extends EntityRepository<User> {

    Optional<User> findByUsername(String username);
}
