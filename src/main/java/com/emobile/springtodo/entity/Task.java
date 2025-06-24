package com.emobile.springtodo.entity;

import com.emobile.springtodo.annotation.DatabaseField;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
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

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public Long getUserId() {
        return userId;
    }

    public Status getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }



}
