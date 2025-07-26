package com.emobile.springtodo.service;

import com.emobile.springtodo.entity.CommonEntity;
import com.emobile.springtodo.exception.EntityNotFoundException;
import com.emobile.springtodo.repository.AbstractRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
public abstract class AbstractService<ENTITY extends CommonEntity> {

    private final AbstractRepository<ENTITY> entityRepository;

    @Transactional
    public void save(ENTITY entity) {
        entityRepository.save(entity);
    }

    @Transactional
    public void delete(long id) {
        var entity = entityRepository.findById(id);
        if (entity != null) {
            entityRepository.delete(entity);
        } else throw new EntityNotFoundException(String.format("Entity with id %s not found", id));
    }

    @Transactional
    public void update(ENTITY entity) {
        entityRepository.findById(entity.getId());
        entityRepository.update(entity);
    }

    @Transactional(readOnly = true)
    public List<ENTITY> findAll(int page, int size) {
        int offset = page * size;
        return entityRepository.findAll(size, offset);
    }

    @Transactional(readOnly = true)
    public ENTITY findById(long id) {
        return entityRepository.findById(id);
    }
}
