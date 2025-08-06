package com.emobile.springtodo.dto.input;

import com.emobile.springtodo.entity.Status;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record TaskRequest(String title,
                          String description,
                          Status status,
                          @Positive(message = "can not be less than 1") Long userId) implements EntityRequest {
}
