package com.emobile.springtodo.entity;

import com.emobile.springtodo.annotation.DatabaseField;

public record Task(Long id,
                   @DatabaseField String title,
                   @DatabaseField String description,
                   @DatabaseField Status status,
                   @DatabaseField Long user_id) implements Entity {
    @Override
    public Long getId() {
        return id;
    }
}
