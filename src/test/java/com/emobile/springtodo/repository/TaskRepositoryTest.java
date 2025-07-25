package com.emobile.springtodo.repository;

import com.emobile.springtodo.entity.Status;
import com.emobile.springtodo.entity.Task;
import com.emobile.springtodo.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("Тесты TaskRepository")
@Testcontainers
@DataJpaTest
public class TaskRepositoryTest {

    @Container
    @ServiceConnection
    public static PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:16");

    @Autowired
    private TaskRepository taskRepository;


    @Test
    @Sql({"/sql/init-users.sql", "/sql/init-tasks.sql"})
    @DisplayName("findById() возвращает корректное дело если оно существует")
    void findById_shouldReturnUser_WhenUserExists() {
        Task task = taskRepository.findById(1L).get();

        assertThat(task.getId()).isEqualTo(1L);
        assertThat(task.getTitle()).isEqualTo("Task1");
        assertThat(task.getDescription()).isEqualTo("Description for task1");
        assertThat(task.getStatus()).isEqualTo(Status.CREATED);
        assertThat(task.getUser().getId()).isEqualTo(1L);
    }

    @Test
    @Sql({"/sql/init-users.sql", "/sql/init-tasks.sql"})
    @DisplayName("findAllForUser() возвращает список дел пользователя")
    void findAllForUser_shouldReturnListOfTasks_whenTasksExist() {
        List<Task> tasks = taskRepository.findByUserId(1L, PageRequest.of(0, 10)).getContent();

        assertThat(tasks).hasSize(2);
        assertThat(tasks.get(0).getUser().getId()).isEqualTo(1L);
        assertThat(tasks.get(1).getUser().getId()).isEqualTo(1L);
        assertThat(tasks.get(0).getTitle()).isEqualTo("Task1");
        assertThat(tasks.get(1).getTitle()).isEqualTo("Task2");
    }

    @Test
    @Sql({"/sql/init-users.sql", "/sql/init-tasks.sql"})
    @DisplayName("findAllForUser() возвращает правильный список дел пользователя с ограничением")
    void findAllForUser_shouldReturnCorrectListWithLimitAndOffset_whenTasksExist() {
        List<Task> tasks = taskRepository.findByUserId(1L, PageRequest.of(0, 1)).getContent();


        assertThat(tasks).hasSize(1);
        assertThat(tasks.getFirst().getId()).isEqualTo(1L);
        assertThat(tasks.getFirst().getTitle()).isEqualTo("Task1");
        assertThat(tasks.getFirst().getDescription()).isEqualTo("Description for task1");
        assertThat(tasks.getFirst().getStatus()).isEqualTo(Status.CREATED);
        assertThat(tasks.getFirst().getUser().getId()).isEqualTo(1L);
    }

    @Test
    @Sql("/sql/init-users.sql")
    @DisplayName("save() устанавливает статус CREATED, если статус не задан при создании")
    void save_shouldSetStatusToCreated_whenStatusNotSpecified() {
        Task newTask = createTestTask();
        newTask.setStatus(null);

        taskRepository.save(newTask);
        List<Task> createdTask = taskRepository.findByUserId(1L, PageRequest.of(0, 10)).getContent();
        assertThat(createdTask).hasSize(1);
        assertThat(createdTask.getFirst().getStatus()).isEqualTo(Status.CREATED);
    }

    @Test
    @Sql("/sql/init-users.sql")
    @DisplayName("save() сохраняет статус заданный при создании")
    void save_shouldPreserveStatus_whenStatusSpecified() {
        Task newTask = createTestTask();
        newTask.setStatus(Status.IN_PROGRESS);

        taskRepository.save(newTask);

        List<Task> tasks = taskRepository.findByUserId(1L, PageRequest.of(0, 10)).getContent();
        assertThat(tasks).hasSize(1);
        Task savedTask = tasks.getFirst();
        assertThat(savedTask.getStatus()).isEqualTo(Status.IN_PROGRESS);
    }

    @Test
    @Sql({"/sql/init-users.sql", "/sql/init-tasks.sql"})
    @DisplayName("update() корректно обновляет дело")
    void update_shouldUpdateTask() {
        Task task = taskRepository.findById(1L).get();
        task.setTitle("UpdatedTask");
        task.setStatus(Status.COMPLETED);

        taskRepository.save(task);

        Task updatedTask = taskRepository.findById(1L).get();
        assertThat(updatedTask.getTitle()).isEqualTo("UpdatedTask");
        assertThat(updatedTask.getStatus()).isEqualTo(Status.COMPLETED);
    }

    @Test
    @Sql({"/sql/init-users.sql", "/sql/init-tasks.sql"})
    @DisplayName("existsById() возвращает true если дело существует")
    void existsById_shouldReturnTrue_whenTaskExists() {
        assertThat(taskRepository.existsById(1L)).isTrue();
    }

    @Test
    @DisplayName("existsById() возвращает false если дело не существует")
    void existsById_shouldReturnFalse_whenTaskNotExist() {
        assertThat(taskRepository.existsById(1L)).isFalse();
    }

    @Test
    @Sql({"/sql/init-users.sql", "/sql/init-tasks.sql"})
    @DisplayName("delete() корректно удаляет дело")
    void delete_shouldDeleteTask() {
        Long taskId = 1L;
        assertThat(taskRepository.existsById(taskId)).isTrue();

        taskRepository.deleteById(taskId);

        assertThat(taskRepository.existsById(taskId)).isFalse();
    }


    private Task createTestTask() {
        Task task = new Task();
        task.setTitle("Test task");
        task.setDescription("Test description");
        var user = new User();
        user.setId(1L);
        task.setUser(user);
        return task;
    }
}
