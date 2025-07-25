package com.emobile.springtodo.repository;

import com.emobile.springtodo.entity.User;
import com.emobile.springtodo.exception.EntityNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepository extends AbstractRepository<User> {
    public UserRepository(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public List<User> findAll(int limit, int offset) {
        String jpql = "SELECT u FROM User u";
        TypedQuery<User> query = entityManager.createQuery(jpql, User.class);
        query.setFirstResult(offset);
        query.setMaxResults(limit);
        List<User> users = query.getResultList();
        if (users.isEmpty()) {
            throw new EntityNotFoundException("Tasks for your request are not found");
        }
        return users;
    }


    public User findByUsername(String username) {
        try {
            String jpql = "SELECT u FROM User u WHERE u.username = :username";
            TypedQuery<User> query = entityManager.createQuery(jpql, User.class);
            query.setParameter("username", username);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
