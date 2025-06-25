package com.emobile.springtodo.api.swagger;

import com.emobile.springtodo.dto.input.TaskRequest;
import com.emobile.springtodo.dto.output.TaskResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "Task controller", description = "Контроллер для доступа к делам")
public interface ITaskController {

    @Operation(
            summary = "Получение страницы дел",
            description = "Получение страницы дел для пользователя по id пользователя",
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
                                                                     "title": "Task 1",
                                                                     "description": "Description for Task 1",
                                                                     "status": "CANCELLED",
                                                                     "userId": 6
                                                                 },
                                                                 {
                                                                     "id": 2,
                                                                     "title": "Task 2",
                                                                     "description": "Description for Task 2",
                                                                     "status": "CANCELLED",
                                                                     "userId": 6
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
    List<TaskResponse> getTasksForUser(long id, @Parameter(description = "Номер страницы") int page,
                                       @Parameter(description = "Размер страницы") int size);

    @Operation(
            summary = "Получение страницы дел",
            description = "Получение страницы дел для всех пользователей",
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
                                                                     "title": "Task 1",
                                                                     "description": "Description for Task 1",
                                                                     "status": "CANCELLED",
                                                                     "userId": 6
                                                                 },
                                                                 {
                                                                     "id": 2,
                                                                     "title": "Task 2",
                                                                     "description": "Description for Task 2",
                                                                     "status": "CANCELLED",
                                                                     "userId": 6
                                                                 },
                                                                 {
                                                                     "id": 3,
                                                                     "title": "Task 3",
                                                                     "description": "Description for Task 3",
                                                                     "status": "CANCELLED",
                                                                     "userId": 7
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
    List<TaskResponse> findAll(@Parameter(description = "Номер страницы") int page,
                               @Parameter(description = "Размер страницы") int size);

    @Operation(
            summary = "Получение дела",
            description = "Получение одного дела по его id",
            responses = {
                    @ApiResponse(
                            description = "Дело найдено",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                        "id": 1,
                                                                        "title": "Task 1",
                                                                        "description": "Description for Task 1",
                                                                        "status": "CANCELLED",
                                                                        "userId": 6
                                                            }
                                                            """
                                            )
                                    }

                            )),
                    @ApiResponse(
                            description = "Дело не найдено",
                            responseCode = "400",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                   "message": "Entity for your request is not found"
                                                            }
                                                            """
                                            )
                                    }
                            ))
            }
    )
    TaskResponse findById(long id);

    @Operation(
            summary = "Сохранение дела",
            description = "Сохраняет дело для существующего пользователя",
            responses = {
                    @ApiResponse(
                            description = "Отзыв сохранен",
                            responseCode = "201"),
                    @ApiResponse(
                            description = "Попытка сохранения дела для несуществующего пользователя",
                            responseCode = "400",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "message": "User with id 34 not found"
                                                            }
                                                            """
                                            )
                                    }
                            )
                    )

            }
    )
    void create(TaskRequest taskRequest);

    @Operation(
            summary = "Обновление дела",
            description = "Обновляет дело по его id",
            responses = {
                    @ApiResponse(
                            description = "Дело обновлено",
                            responseCode = "200"),
                    @ApiResponse(
                            description = "Попытка изменения пользователя на несуществующего",
                            responseCode = "400",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "message": "User with id 34 not found"
                                                            }
                                                            """
                                            )
                                    }
                            )
                    )

            }
    )
    void update(long id, TaskRequest taskRequest);

    @Operation(
            summary = "Удаление дела",
            description = "Удаление дела по его id",
            responses = {
                    @ApiResponse(
                            description = "Отзыв удален",
                            responseCode = "204"
                    )
            }
    )
    void delete(long id);
}
