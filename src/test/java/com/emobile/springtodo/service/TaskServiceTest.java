package com.emobile.springtodo.service;

import com.emobile.springtodo.entity.Status;
import com.emobile.springtodo.entity.Task;
import com.emobile.springtodo.entity.User;
import com.emobile.springtodo.exception.EntityNotFoundException;
import com.emobile.springtodo.repository.TaskRepository;
import com.emobile.springtodo.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Тесты TaskService")
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TaskService taskService;

    @Test
    @DisplayName("findAll() возвращает список дел")
    void findAll_shouldReturnListOfTasks() {
        var expectedTasks = List.of(createTask(1L, 1L), createTask(2L, 2L), createTask(3L, 3L));
        when(taskRepository.findAll(PageRequest.of(0, 3))).thenReturn(new PageImpl<>(expectedTasks));

        List<Task> result = taskService.findAll(0, 3);

        assertEquals(expectedTasks, result);
    }

    @Test
    @DisplayName("findAllForUser() возвращает список дел для пользователя")
    void findAllForUser_shouldReturnUserTasks() {
        List<Task> expectedTasks = List.of(createTask(1L, 1L), createTask(2L, 1L));
        when(taskRepository.findByUserId(1L, PageRequest.of(0, 2))).thenReturn(new PageImpl<>(expectedTasks));

        List<Task> result = taskService.findAllForUser(1L, 0, 2);

        assertEquals(expectedTasks, result);
    }

    @Test
    @DisplayName("save() сохраняет дело для существующего пользователя")
    void save_shouldSaveTask_whenUserExists() {
        Task task = createTask(1L, 1L);
        when(userRepository.existsById(1L)).thenReturn(true);

        taskService.save(task);

        verify(taskRepository).save(task);
    }

    @Test
    @DisplayName("save() генерирует исключение при попытке сохранить дело для несуществующего пользователя")
    void save_shouldThrowException_whenUserNotExist() {
        Task task = createTask(1L, 1L);
        when(userRepository.existsById(1L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> taskService.save(task));
    }

    @Test
    @DisplayName("save() генерирует исключение при попытке сохранить дело для пользователя с null идентификатором")
    void save_shouldThrowException_whenUserIdIsNull() {
        Task task = createTask(1L, null);

        assertThrows(EntityNotFoundException.class, () -> taskService.save(task));
    }

    @Test
    @DisplayName("update() обновляет дело для существующего пользователя")
    void update_shouldUpdateTask_whenUserExists() {
        Task task = createTask(1L, 1L);
        when(userRepository.existsById(1L)).thenReturn(true);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        taskService.update(task);

        verify(taskRepository).save(task);
    }

    @Test
    @DisplayName("update() обновляет дело с null идентификатором пользователя")
    void update_shouldUpdateTask_whenUserIdIsNull() {
        Task task = createTask(1L, null);
        task.getUser().setId(null);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        taskService.update(task);

        verify(taskRepository).save(task);
    }

    @Test
    @DisplayName("update() генерирует исключение при попытке присвоения делу несуществующего пользователя")
    void update_shouldThrowException_whenUserNotExist() {
        Task task = createTask(1L, 1L);
        when(userRepository.existsById(1L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> taskService.update(task));
    }

    @Test
    @DisplayName("findById() возвращает задачу, если она существует")
    void findById_shouldReturnTask_whenTaskExists() {
        Task expectedTask = createTask(1L, 1L);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(expectedTask));

        Task result = taskService.findById(1L);

        assertEquals(expectedTask, result);
    }

    @Test
    @DisplayName("delete() удаляет задачу, если она существует")
    void delete_shouldDeleteTask_whenTaskExists() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(createTask(1L, 1L)));

        taskService.delete(1L);

        verify(taskRepository).delete(any());
    }

    @Test
    @DisplayName("delete() генерирует исключение при попытке удалить несуществующую задачу")
    void delete_shouldThrowException_whenTaskNotExist() {
        assertThrows(EntityNotFoundException.class, () -> taskService.delete(1L));
    }

    private Task createTask(Long id, Long userId) {
        Task task = new Task();
        task.setId(id);
        task.setTitle("Test Task " + id);
        task.setDescription("Test Description " + id);
        task.setStatus(Status.CREATED);
        var user = new User();
        user.setId(userId);
        task.setUser(user);
        return task;
    }
}


