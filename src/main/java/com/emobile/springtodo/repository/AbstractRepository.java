package com.emobile.springtodo.repository;

import com.emobile.springtodo.entity.CommonEntity;
import com.emobile.springtodo.exception.EntityNotFoundException;
import jakarta.persistence.EntityManager;

import java.lang.reflect.ParameterizedType;
import java.util.List;

public abstract class AbstractRepository<T extends CommonEntity> {
    protected final EntityManager entityManager;
    private final Class<T> entityClass;

    @SuppressWarnings("unchecked")
    public AbstractRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
                .getActualTypeArguments()[0];
    }

    public abstract List<T> findAll(int limit, int offset);

    public void save(T entity) {
        entityManager.persist(entity);
    }

    public void delete(T entity) {
        entityManager.remove(entity);
    }

    public T findById(Long id) {
        T entity = entityManager.find(entityClass, id);
        if (entity == null) {
            throw new EntityNotFoundException("Entity for your request is not found");
        }
        return entity;
    }

    public void update(T entity) {
        entityManager.merge(entity);
    }

    public boolean existsById(Long id) {
        try {
            findById(id);
            return true;
        } catch (EntityNotFoundException e) {
            return false;
        }
    }
}
