package com.emobile.springtodo;



import com.emobile.springtodo.controller.TaskController;
import com.emobile.springtodo.controller.UserController;
import com.emobile.springtodo.repository.TaskRepository;
import com.emobile.springtodo.repository.UserRepository;
import com.emobile.springtodo.service.TaskService;
import com.emobile.springtodo.service.UserService;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Testcontainers
class SpringToDoApplicationTests {

    @Container
    @ServiceConnection
    public static PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:16");

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private TaskService taskService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserController userController;
    @Autowired
    private TaskController taskController;



    @Test
    void contextLoads() {
        assertNotNull(userRepository);
        assertNotNull(taskRepository);
        assertNotNull(taskService);
        assertNotNull(userService);
        assertNotNull(userController);
        assertNotNull(taskController);
    }

}
