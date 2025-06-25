package com.emobile.springtodo.api.swagger;

import com.emobile.springtodo.dto.input.UserRequest;
import com.emobile.springtodo.dto.output.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "User controller", description = "Контроллер для доступа к пользователям")
public interface IUserController {

    @Operation(
            summary = "Получение пользователя",
            description = "Получение пользователя по его id",
            responses = {
                    @ApiResponse(
                            description = "Пользователь найден",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value = """
                                                    {
                                                    "id": 1,
                                                    "username": 'User1'
                                                    }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            description = "Пользователь не найден",
                            responseCode = "400",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value = """
                                                     {
                                                     "message": "Entity for your request is not found"
                                                     }
                                                    """
                                    )
                            )
                    )}
    )
    UserResponse findById(long id);

    @Operation(
            summary = "Удаление пользователя",
            description = "Удаление пользователя по его id",
            responses = @ApiResponse(
                    description = "Пользователь удален",
                    responseCode = "204"
            )
    )
    void delete(long id);

    @Operation(
            summary = "Создание пользователя"
    )
    void create(UserRequest userRequest);
}
