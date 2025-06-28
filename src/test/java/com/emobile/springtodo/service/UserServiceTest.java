package com.emobile.springtodo.service;

import com.emobile.springtodo.entity.User;
import com.emobile.springtodo.exception.EntityExistsException;
import com.emobile.springtodo.exception.EntityNotFoundException;
import com.emobile.springtodo.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Тесты UserService")
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("save() сохраняет пользователя если его имя не существовало ранее")
    void save_shouldSaveUser_whenUserNotExist() {
        User user = createUser(1L, "testUser");
        when(userRepository.findByUsername("testUser")).thenReturn(null);

        userService.save(user);

        verify(userRepository).save(user);
    }

    @Test
    @DisplayName("save() генерирует исключение при попытке создать пользователя с уже существующим именем")
    void save_shouldThrowException_whenUserWithUsernameExists() {
        User existingUser = createUser(1L, "testUser");
        User newUser = createUser(2L, "testUser");
        when(userRepository.findByUsername("testUser")).thenReturn(existingUser);

        assertThrows(EntityExistsException.class, () -> userService.save(newUser));
    }

    @Test
    @DisplayName("findById() возвращает пользователя, если он существует")
    void findById_shouldReturnUser_whenUserExists() {
        User expectedUser = createUser(1L, "testUser");
        when(userRepository.findById(1L)).thenReturn(expectedUser);

        User result = userService.findById(1L);

        assertEquals(expectedUser, result);
    }

    @Test
    @DisplayName("findAll() возвращает список пользователей")
    void findAll_shouldReturnListOfUsers() {
        List<User> expectedUsers = List.of(
                createUser(1L, "user1"),
                createUser(2L, "user2")
        );
        when(userRepository.findAll(2, 0)).thenReturn(expectedUsers);

        List<User> result = userService.findAll(0, 2);

        assertEquals(expectedUsers, result);
    }

    @Test
    @DisplayName("delete() удаляет пользователя, если он существует")
    void delete_shouldDeleteUser_whenUserExists() {
        when(userRepository.existsById(1L)).thenReturn(true);

        userService.delete(1L);

        verify(userRepository).delete(1L);
    }

    @Test
    @DisplayName("delete() генерирует исключение при попытке удалить несуществующего пользователя")
    void delete_shouldThrowException_whenUserNotExist() {
        when(userRepository.existsById(1L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> userService.delete(1L));
    }

    @Test
    @DisplayName("update() обновляет пользователя если новое имя не существовало ранее")
    void update_shouldUpdateUser_whenUsernameNotExist() {
        User user = createUser(1L, "newUsername");
        when(userRepository.findByUsername("newUsername")).thenReturn(null);
        when(userRepository.findById(1L)).thenReturn(user);

        userService.update(user);

        verify(userRepository).update(user);
    }

    @Test
    @DisplayName("update() генерирует исключение если новое имя пользователя уже существует")
    void update_shouldThrowException_whenUsernameAlreadyExists() {
        User existingUser = createUser(2L, "existingUsername");
        User userToUpdate = createUser(1L, "existingUsername");
        when(userRepository.findByUsername("existingUsername")).thenReturn(existingUser);

        assertThrows(EntityExistsException.class, () -> userService.update(userToUpdate));
    }

    private User createUser(Long id, String username) {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        return user;
    }
}
