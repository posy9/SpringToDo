package com.emobile.springtodo.dto.mapper;

import com.emobile.springtodo.dto.Dto;
import com.emobile.springtodo.entity.CommonEntity;

public interface BaseMapper<ENTITY extends CommonEntity, DTO extends Dto> {
    DTO toDto(ENTITY entity);

    ENTITY toEntity(DTO dto);
}
