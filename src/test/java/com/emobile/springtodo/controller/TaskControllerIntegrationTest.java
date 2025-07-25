package com.emobile.springtodo.controller;

import com.emobile.springtodo.dto.input.TaskRequest;
import com.emobile.springtodo.entity.Status;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@DisplayName("Интеграционные тесты для TaskController")
@AutoConfigureMockMvc
@Testcontainers
public class TaskControllerIntegrationTest {

    @Container
    @ServiceConnection
    public static PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:16");
    @Container
    @ServiceConnection
    static GenericContainer<?> redisContainer = new GenericContainer<>("redis:8").withExposedPorts(6379);
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @BeforeEach
    void cleanRedis() {
        redisTemplate.getConnectionFactory().getConnection().serverCommands().flushDb();
    }

    @Test
    @Sql({"/sql/init-users.sql", "/sql/init-tasks.sql"})
    @Sql(scripts = "/sql/clean-db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("GET /tasks/{id} возвращает дело по id")
    void findById_shouldReturnTask_whenTaskExists() throws Exception {
        String expectedJson = """
                 {
                             "id": 1,
                             "title": "Task1",
                             "description": "Description for task1",
                             "status": "CREATED",
                             "userId": 1
                           }
                """;

        MvcResult result = mockMvc.perform(get("/tasks/{id}", 1L))
                .andExpect(status().isOk())
                .andReturn();
        String actualJson = result.getResponse().getContentAsString();
        JSONAssert.assertEquals(expectedJson, actualJson, JSONCompareMode.STRICT);
    }

    @Test
    @DisplayName("GET /tasks/{id} возвращает 400 если дело с id не найдено")
    void findById_shouldReturnError_whenTaskNotExist() throws Exception {
        String expectedJson = """
                 {
                         "message": "Entity for your request is not found"
                           }
                """;

        MvcResult result = mockMvc.perform(get("/tasks/{id}", 1L))
                .andExpect(status().isBadRequest())
                .andReturn();
        String actualJson = result.getResponse().getContentAsString();
        JSONAssert.assertEquals(expectedJson, actualJson, JSONCompareMode.STRICT);
    }

    @Test
    @Sql("/sql/init-users.sql")
    @Sql(scripts = "/sql/clean-db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("POST /tasks создает дело для существующего пользователя")
    void create_shouldCreateTaskForExistingUser() throws Exception {
        TaskRequest newTask = TaskRequest.builder()
                .title("newTask")
                .description("Description for newTask")
                .status(Status.CREATED)
                .userId(1L)
                .build();
        String requestJson = objectMapper.writeValueAsString(newTask);

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpectAll(
                        status().isCreated());


    }

    @Test
    @DisplayName("POST /tasks возвращает 400 если дело создается для несуществующего пользователя")
    void create_shouldReturnErrorForNonExistentUser() throws Exception {
        TaskRequest newTask = TaskRequest.builder()
                .title("newTask")
                .description("Description for newTask")
                .status(Status.CREATED)
                .userId(1L)
                .build();
        String requestJson = objectMapper.writeValueAsString(newTask);

        String expectedJson = """
                {
                message: "User with id 1 not found"
                }
                """;

        MvcResult badRequestResult = mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpectAll(
                        status().isBadRequest())
                .andReturn();

        String resultJson = badRequestResult.getResponse().getContentAsString();
        JSONAssert.assertEquals(expectedJson, resultJson, JSONCompareMode.STRICT);
    }

    @Test
    @Sql({"/sql/init-users.sql", "/sql/init-tasks.sql"})
    @Sql(scripts = "/sql/clean-db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("GET /tasks возвращает список дел")
    void findAll_shouldFindAllTasks() throws Exception {
        String expectedJson = """
                          [
                           {
                             "id": 1,
                             "title": "Task1",
                             "description": "Description for task1",
                             "status": "CREATED",
                             "userId": 1
                           },
                           {
                             "id": 2,
                             "title": "Task2",
                             "description": "Description for task2",
                             "status": "CANCELLED",
                             "userId": 1
                           },
                           {
                             "id": 3,
                             "title": "Task3",
                             "description": "Description for task3",
                             "status": "CREATED",
                             "userId": 2
                           },
                           {
                             "id": 4,
                             "title": "Task4",
                             "description": "Description for task4",
                             "status": "COMPLETED",
                             "userId": 2
                           },
                           {
                             "id": 5,
                             "title": "Task5",
                             "description": "Description for task6",
                             "status": "CREATED",
                             "userId": 3
                           },
                           {
                             "id": 6,
                             "title": "Task6",
                             "description": "Description for task6",
                             "status": "COMPLETED",
                             "userId": 3
                           }
                         ]
                """;

        MvcResult result = mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String actualJson = result.getResponse().getContentAsString();
        JSONAssert.assertEquals(expectedJson, actualJson, JSONCompareMode.STRICT);
    }

    @Test
    @Sql({"/sql/init-users.sql", "/sql/init-tasks.sql"})
    @Sql(scripts = "/sql/clean-db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("GET /tasks/user/{id} возвращает список дел для пользователя")
    void getTasksForUser_shouldFindTasksForUser() throws Exception {
        String expectedJson = """
                          [
                           {
                             "id": 1,
                             "title": "Task1",
                             "description": "Description for task1",
                             "status": "CREATED",
                             "userId": 1
                           },
                           {
                             "id": 2,
                             "title": "Task2",
                             "description": "Description for task2",
                             "status": "CANCELLED",
                             "userId": 1
                           }
                         ]
                """;

        MvcResult result = mockMvc.perform(get("/tasks/user/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String actualJson = result.getResponse().getContentAsString();
        JSONAssert.assertEquals(expectedJson, actualJson, JSONCompareMode.STRICT);
    }

    @Test
    @DisplayName("GET /tasks возвращает 400 если дела не найдены")
    void findAll_shouldReturnError_whenTasksNotFound() throws Exception {
        String expectedJson = """
                {
                message: "Tasks for your request are not found"
                }
                """;
        MvcResult badRequestResult = mockMvc.perform(get("/tasks"))
                .andExpect(status().isBadRequest())
                .andReturn();

        String actualJson = badRequestResult.getResponse().getContentAsString();
        JSONAssert.assertEquals(expectedJson, actualJson, JSONCompareMode.STRICT);
    }

    @Test
    @Sql("/sql/init-users.sql")
    @Sql(scripts = "/sql/clean-db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("GET /tasks/user/{id} возвращает 400 если дела для пользователя не найдены")
    void findAllForUser_shouldReturnError_whenTasksForUserNotFound() throws Exception {
        String expectedJson = """
                {
                message: "Tasks for your request are not found"
                }
                """;
        MvcResult badRequestResult = mockMvc.perform(get("/tasks/user/{id}", 2L))
                .andExpect(status().isBadRequest())
                .andReturn();

        String actualJson = badRequestResult.getResponse().getContentAsString();
        JSONAssert.assertEquals(expectedJson, actualJson, JSONCompareMode.STRICT);
    }

    @Test
    @DisplayName("GET /tasks/user/{id} возвращает 400 если пользователь не найден")
    void findAllForUser_shouldReturnError_whenUserNotFound() throws Exception {
        String expectedJson = """
                {
                message: "Tasks for your request are not found"
                }
                """;
        MvcResult badRequestResult = mockMvc.perform(get("/tasks/user/{id}", 2L))
                .andExpect(status().isBadRequest())
                .andReturn();

        String actualJson = badRequestResult.getResponse().getContentAsString();
        JSONAssert.assertEquals(expectedJson, actualJson, JSONCompareMode.STRICT);
    }

    @Test
    @Sql({"/sql/init-users.sql", "/sql/init-tasks.sql"})
    @Sql(scripts = "/sql/clean-db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("PUT /tasks/{id} возвращает 400 при попытке обновить пользователя на несуществующего")
    void update_shouldReturnErrorForNonExistedUser() throws Exception {
        TaskRequest updatedTask = TaskRequest.builder()
                .title("UpdatedTitle")
                .status(Status.COMPLETED)
                .userId(99L)
                .build();

        String requestJson = objectMapper.writeValueAsString(updatedTask);
        String expectedJson = """
                 {
                 "message":"User with id 99 not found"
                 }
                """;

        MvcResult badRequestResult = mockMvc.perform(put("/tasks/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andReturn();
        String actualJson = badRequestResult.getResponse().getContentAsString();
        JSONAssert.assertEquals(expectedJson, actualJson, JSONCompareMode.STRICT);
    }

    @Test
    @Sql({"/sql/init-users.sql", "/sql/init-tasks.sql"})
    @Sql(scripts = "/sql/clean-db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("DELETE /tasks/{id} удаляет дело")
    void delete_shouldDeleteTask_whenTaskExists() throws Exception {
        mockMvc.perform(delete("/tasks/{id}", 1L))
                .andExpect(status().isNoContent());

    }

    @Test
    @DisplayName("DELETE /tasks/{id} возвращает 400 при попытке удаления несуществующего дела")
    void delete_shouldReturnError_whenTaskNotExist() throws Exception {
        String expectedJson = """
                 {
                 "message":"Task with id 1 not found"
                 }
                """;

        MvcResult badRequestResult = mockMvc.perform(delete("/tasks/{id}", 1L))
                .andExpect(status().isBadRequest())
                .andReturn();
        String actualJson = badRequestResult.getResponse().getContentAsString();
        JSONAssert.assertEquals(expectedJson, actualJson, JSONCompareMode.STRICT);
    }

}
