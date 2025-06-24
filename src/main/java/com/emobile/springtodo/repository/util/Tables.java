package com.emobile.springtodo.repository.util;

public enum Tables {
    USERS,
    TASKS;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
