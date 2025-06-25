package com.emobile.springtodo.dto.input;

import jakarta.validation.constraints.NotNull;

public record UserRequest(@NotNull(message = "should not be null") String username) implements EntityRequest {
}

