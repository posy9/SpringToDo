package com.emobile.springtodo.repository.util;

public enum Tables {
    USER,
    TASK;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
