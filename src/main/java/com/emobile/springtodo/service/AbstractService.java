package com.emobile.springtodo.service;

import com.emobile.springtodo.entity.Entity;
import com.emobile.springtodo.exception.EntityNotFoundException;
import com.emobile.springtodo.repository.AbstractRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
public abstract class AbstractService<ENTITY extends Entity> {

    private final AbstractRepository<ENTITY> entityRepository;

    @Transactional
    public void save(ENTITY entity) {
        entityRepository.save(entity);
    }

    @Transactional
    public void delete(long id) {
        entityRepository.delete(id);
    }

    @Transactional
    public void update(ENTITY entity) {
        entityRepository.findById(entity.getId());
        entityRepository.update(entity);
    }

    @Transactional(readOnly = true)
    public List<ENTITY> findAll(int page, int size) {
        int offset = page * size;
        var entities = entityRepository.findAll(size, offset);
        if (entities.isEmpty()) {
            throw new EntityNotFoundException("Entities for your request are not found");
        }
        return entities;
    }

    @Transactional(readOnly = true)
    public ENTITY findById(long id) {
        return entityRepository.findById(id);
    }
}
