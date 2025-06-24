package com.emobile.springtodo.entity;

import com.emobile.springtodo.annotation.DatabaseField;

public record User(Long id, @DatabaseField String username) implements Entity {
    @Override
    public Long getId() {
        return id;
    }
}
