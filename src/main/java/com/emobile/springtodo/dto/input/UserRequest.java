package com.emobile.springtodo.dto.input;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record UserRequest(@NotNull(message = "should not be null") String username) implements EntityRequest {
}

