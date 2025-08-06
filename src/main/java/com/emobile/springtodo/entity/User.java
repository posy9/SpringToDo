package com.emobile.springtodo.entity;

import com.emobile.springtodo.annotation.DatabaseField;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
}
