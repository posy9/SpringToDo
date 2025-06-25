package com.emobile.springtodo.api.swagger;

import com.emobile.springtodo.dto.input.TaskRequest;
import com.emobile.springtodo.dto.input.UserRequest;
import com.emobile.springtodo.dto.output.TaskResponse;
import com.emobile.springtodo.dto.output.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

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
            summary = "Получение страницы пользователей",
            description = "Получение страницы пользователей",
            responses = {
                    @ApiResponse(
                            description = "Страница найдена",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            [
                                                                 {
                                                                     "id": 1,
                                                                     "username": "User1"
                                                                 },
                                                                 {
                                                                     "id": 2,
                                                                     "username": "User2"
                                                                 },
                                                                 {
                                                                     "id": 3,
                                                                     "username": "User3"
                                                                 }
                                                            ]
                                                            """
                                            )
                                    }

                            )),
                    @ApiResponse(
                            description = "Страница не найдена",
                            responseCode = "400",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "message": "Entities for your request are not found"
                                                            }
                                                            """
                                            )
                                    }
                            ))
            }
    )
    List<UserResponse> findAll(@Parameter(description = "Номер страницы") int page,
                               @Parameter(description = "Размер страницы") int size);


    @Operation(
            summary = "Сохранение пользователя",
            description = "Сохраняет пользователя",
            responses = {
                    @ApiResponse(
                            description = "Пользователь сохранен",
                            responseCode = "201"),
                    @ApiResponse(
                            description = "Попытка сохранения пользователя, который уже существует",
                            responseCode = "400",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "message": "User Alex already exists"
                                                            }
                                                            """
                                            )
                                    }
                            )
                    )

            }
    )
    void create(UserRequest userRequest);

    @Operation(
            summary = "Обновление пользователя",
            description = "Обновляет пользователя по его id",
            responses = {
                    @ApiResponse(
                            description = "Пользователь обновлен",
                            responseCode = "200"),
                    @ApiResponse(
                            description = "Попытка изменения имени на уже существующего",
                            responseCode = "400",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "message": "User Alex already exists"
                                                            }
                                                            """
                                            )
                                    }
                            )
                    )

            }
    )
    void update(long id, UserRequest taskRequest);

    @Operation(
            summary = "Удаление пользователя",
            description = "Удаление пользователя по его id",
            responses = {
                    @ApiResponse(
                            description = "Пользователь удален",
                            responseCode = "204"
                    ),
                    @ApiResponse(
                            description = "Пользователь с указанным id не  найден",
                            responseCode = "400",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "message": "User with id 323 not found"
                                                            }
                                                            """
                                            )
                                    }
                            )
                    )
            }
    )
    void delete(long id);
}
