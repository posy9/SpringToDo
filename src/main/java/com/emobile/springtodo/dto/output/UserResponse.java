package com.emobile.springtodo.dto.output;

public record UserResponse(long id,
                           String username) implements EntityResponse {
}
