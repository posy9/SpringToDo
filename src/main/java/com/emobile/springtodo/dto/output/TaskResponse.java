package com.emobile.springtodo.dto.output;

import com.emobile.springtodo.entity.Status;

public record TaskResponse(long id,
                           String title,
                           String description,
                           Status status,
                           Long userId) implements EntityResponse {
}
