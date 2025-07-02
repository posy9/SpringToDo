package com.emobile.springtodo.controller;

import com.emobile.springtodo.dto.input.UserRequest;
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
@DisplayName("Интеграционные тесты для UserController")
@AutoConfigureMockMvc
@Testcontainers
public class UserControllerIntegrationTest {

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
    @Sql("/sql/init-users.sql")
    @Sql(scripts = "/sql/clean-db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("GET /users/{id} возвращает пользователя по id")
    void findById_shouldReturnTask_whenUserExists() throws Exception {
        String expectedJson = """
                 {
                             "id": 1,
                             "username": "User1"
                 }
                """;

        MvcResult result = mockMvc.perform(get("/users/{id}", 1L))
                .andExpect(status().isOk())
                .andReturn();
        String actualJson = result.getResponse().getContentAsString();
        JSONAssert.assertEquals(expectedJson, actualJson, JSONCompareMode.STRICT);
    }

    @Test
    @DisplayName("GET /users/{id} возвращает 400 если пользователь с id не найден")
    void findById_shouldReturnError_whenUserNotExist() throws Exception {
        String expectedJson = """
                 {
                         "message": "Entity for your request is not found"
                 }
                """;

        MvcResult result = mockMvc.perform(get("/users/{id}", 1L))
                .andExpect(status().isBadRequest())
                .andReturn();
        String actualJson = result.getResponse().getContentAsString();
        JSONAssert.assertEquals(expectedJson, actualJson, JSONCompareMode.STRICT);
    }

    @Test
    @DisplayName("POST /users создает пользователя")
    @Sql(scripts = "/sql/clean-db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void create_shouldCreateTaskForExistingUser() throws Exception {

        UserRequest newUser = UserRequest.builder()
                .username("newUser")
                .build();
        String requestJson = objectMapper.writeValueAsString(newUser);
        String expectedJson = """
                 {
                             "id": 1,
                             "username": "newUser"
                 }
                """;

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpectAll(
                        status().isCreated());

        MvcResult result = mockMvc.perform(get("/users/{id}", 1L))
                .andExpect(status().isOk())
                .andReturn();
        String actualJson = result.getResponse().getContentAsString();
        JSONAssert.assertEquals(expectedJson, actualJson, JSONCompareMode.STRICT);
    }

    @Test
    @DisplayName("POST /users возвращает 400 при попытке создать пользователя с существующим именем")
    @Sql("/sql/init-users.sql")
    @Sql(scripts = "/sql/clean-db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void create_shouldReturnError_whenUsernameExists() throws Exception {

        UserRequest newUser = UserRequest.builder()
                .username("User1")
                .build();
        String requestJson = objectMapper.writeValueAsString(newUser);
        String expectedJson = """
                 {
                         "message": "User User1 already exists"
                 }
                """;

        MvcResult badRequestResult = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpectAll(
                        status().isBadRequest())
                .andReturn();
        String actualJson = badRequestResult.getResponse().getContentAsString();
        JSONAssert.assertEquals(expectedJson, actualJson, JSONCompareMode.STRICT);
    }

    @Test
    @DisplayName("POST /users возвращает 400 при попытке создать пользователя без имени")
    void create_shouldReturnError_whenUsernameIsNull() throws Exception {

        UserRequest newUser = UserRequest.builder()
                .username(null)
                .build();
        String requestJson = objectMapper.writeValueAsString(newUser);
        String expectedJson = """
                 {
                         "username": "should not be null"
                 }
                """;

        MvcResult badRequestResult = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpectAll(
                        status().isBadRequest())
                .andReturn();
        String actualJson = badRequestResult.getResponse().getContentAsString();
        JSONAssert.assertEquals(expectedJson, actualJson, JSONCompareMode.STRICT);
    }

    @Test
    @Sql("/sql/init-users.sql")
    @Sql(scripts = "/sql/clean-db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("GET /users возвращает список пользователей")
    void findAll_shouldFindAllUsers() throws Exception {
        String expectedJson = """
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
                """;

        MvcResult result = mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String actualJson = result.getResponse().getContentAsString();
        JSONAssert.assertEquals(expectedJson, actualJson, JSONCompareMode.STRICT);
    }

    @Test
    @DisplayName("GET /users возвращает 400 если пользователи не найдены")
    void findAll_shouldReturnError_whenUsersNotFound() throws Exception {
        String expectedJson = """
                {
                message: "Entities for your request are not found"
                }
                """;
        MvcResult badRequestResult = mockMvc.perform(get("/users"))
                .andExpect(status().isBadRequest())
                .andReturn();

        String actualJson = badRequestResult.getResponse().getContentAsString();
        JSONAssert.assertEquals(expectedJson, actualJson, JSONCompareMode.STRICT);
    }

    @Test
    @Sql("/sql/init-users.sql")
    @Sql(scripts = "/sql/clean-db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("PUT /users/{id} обновляет пользователя")
    void update_shouldUpdateUser() throws Exception {
        UserRequest updatedUser = UserRequest.builder()
                .username("updatedUser")
                .build();

        String requestJson = objectMapper.writeValueAsString(updatedUser);
        String expectedJson = """
                 {
                             "id": 1,
                             "username": "updatedUser"
                 }
                """;

        mockMvc.perform(put("/users/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());

        MvcResult result = mockMvc.perform(get("/users/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        String actualJson = result.getResponse().getContentAsString();
        JSONAssert.assertEquals(expectedJson, actualJson, JSONCompareMode.STRICT);
    }

    @Test
    @Sql("/sql/init-users.sql")
    @Sql(scripts = "/sql/clean-db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("PUT /users возвращает 400 при попытке обновить пользователя без имени")
    void update_shouldReturnError_whenUsernameIsNull() throws Exception {

        UserRequest newUser = UserRequest.builder()
                .username(null)
                .build();
        String requestJson = objectMapper.writeValueAsString(newUser);
        String expectedJson = """
                 {
                         "username": "should not be null"
                 }
                """;

        MvcResult badRequestResult = mockMvc.perform(put("/users/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpectAll(
                        status().isBadRequest())
                .andReturn();
        String actualJson = badRequestResult.getResponse().getContentAsString();
        JSONAssert.assertEquals(expectedJson, actualJson, JSONCompareMode.STRICT);
    }

    @Test
    @DisplayName("PUT /users возвращает 400 при попытке обновить имя пользователя на существующее")
    @Sql("/sql/init-users.sql")
    @Sql(scripts = "/sql/clean-db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void update_shouldReturnError_whenUsernameExists() throws Exception {

        UserRequest newUser = UserRequest.builder()
                .username("User2")
                .build();
        String requestJson = objectMapper.writeValueAsString(newUser);
        String expectedJson = """
                 {
                         "message": "User User2 already exists"
                 }
                """;

        MvcResult badRequestResult = mockMvc.perform(put("/users/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpectAll(
                        status().isBadRequest())
                .andReturn();
        String actualJson = badRequestResult.getResponse().getContentAsString();
        JSONAssert.assertEquals(expectedJson, actualJson, JSONCompareMode.STRICT);
    }

    @Test
    @Sql("/sql/init-users.sql")
    @Sql(scripts = "/sql/clean-db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("DELETE /users/{id} удаляет пользователя")
    void delete_shouldDeleteTask_whenTaskExists() throws Exception {
        mockMvc.perform(delete("/users/{id}", 1L))
                .andExpect(status().isNoContent());

    }

    @Test
    @DisplayName("DELETE /users/{id} возвращает 400 при попытке удаления несуществующего пользователя")
    void delete_shouldReturnError_whenTaskNotExist() throws Exception {
        String expectedJson = """
                 {
                 "message":"User with id 1 not found"
                 }
                """;

        MvcResult badRequestResult = mockMvc.perform(delete("/users/{id}", 1L))
                .andExpect(status().isBadRequest())
                .andReturn();
        String actualJson = badRequestResult.getResponse().getContentAsString();
        JSONAssert.assertEquals(expectedJson, actualJson, JSONCompareMode.STRICT);
    }
}
