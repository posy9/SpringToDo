package com.emobile.springtodo.entity;

import com.emobile.springtodo.annotation.DatabaseField;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class Task implements Entity {

    private Long id;

    @DatabaseField
    private String title;
    @DatabaseField
    private String description;
    @DatabaseField
    private Status status;
    @DatabaseField
    private Long userId;


    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }
}
