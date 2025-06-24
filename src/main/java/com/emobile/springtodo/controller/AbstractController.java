package com.emobile.springtodo.controller;

import com.emobile.springtodo.dto.input.EntityRequest;
import com.emobile.springtodo.dto.mapper.BaseMapper;
import com.emobile.springtodo.dto.output.EntityResponse;
import com.emobile.springtodo.entity.Entity;
import com.emobile.springtodo.service.AbstractService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
public abstract class AbstractController<ENTITY extends Entity, RESPONSE extends EntityResponse, REQUEST extends EntityRequest> {

    protected final BaseMapper<ENTITY, REQUEST> requestMapper;
    protected final BaseMapper<ENTITY, RESPONSE> responseMapper;
    private final AbstractService<ENTITY> entityService;

    @GetMapping
    List<RESPONSE> findAll(@RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "10") int size) {
        return entityService.findAll(page, size).stream().map(responseMapper::toDto).toList();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    RESPONSE findById(@PathVariable long id) {
        return responseMapper.toDto(entityService.findById(id));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    void create(@RequestBody REQUEST entity) {
        ENTITY entityToCreate = requestMapper.toEntity(entity);
        entityService.save(entityToCreate);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    void update(@PathVariable long id, @RequestBody REQUEST entity) {
        ENTITY entityToUpdate = requestMapper.toEntity(entity);
        entityToUpdate.setId(id);
        entityService.update(entityToUpdate);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable long id) {
        entityService.delete(id);
    }
}
