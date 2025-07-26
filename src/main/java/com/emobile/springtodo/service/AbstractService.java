package com.emobile.springtodo.service;

import com.emobile.springtodo.entity.CommonEntity;
import com.emobile.springtodo.exception.EntityNotFoundException;
import com.emobile.springtodo.repository.EntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
public abstract class AbstractService<ENTITY extends CommonEntity> {

    private final EntityRepository<ENTITY> entityRepository;

    @Transactional
    public void save(ENTITY entity) {
        entityRepository.save(entity);
    }

    @Transactional
    public void delete(long id) {
        var entity = entityRepository.findById(id);
        if(entity.isPresent()) {
            entityRepository.delete(entity.get());
        } else {
            throw new EntityNotFoundException(String.format("Entity with id %s not found", id));
        }
    }

    @Transactional
    public void update(ENTITY entity) {
        entityRepository.findById(entity.getId());
        entityRepository.save(entity);
    }

    @Transactional(readOnly = true)
    public List<ENTITY> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return entityRepository.findAll(pageable).getContent();
    }

    @Transactional(readOnly = true)
    public ENTITY findById(long id) {
        return entityRepository.findById(id).orElse(null);
    }
}
