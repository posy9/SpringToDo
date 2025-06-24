package com.emobile.springtodo.dto.input;

import com.emobile.springtodo.entity.Status;

public record TaskRequest(String title,
                          String description,
                          Status status,
                          Long userId) implements EntityRequest {
}
