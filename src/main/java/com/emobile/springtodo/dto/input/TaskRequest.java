package com.emobile.springtodo.dto.input;

import com.emobile.springtodo.entity.Status;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record TaskRequest(@NotNull String title,
                          String description,
                          Status status,
                          @NotNull Long userId) implements EntityRequest {
}
