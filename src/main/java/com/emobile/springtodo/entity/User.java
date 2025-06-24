package com.emobile.springtodo.entity;

import com.emobile.springtodo.annotation.DatabaseField;

public class User implements Entity {

    private Long id;
    @DatabaseField
    private String username;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
