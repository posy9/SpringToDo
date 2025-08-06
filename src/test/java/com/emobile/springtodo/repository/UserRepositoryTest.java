package com.emobile.springtodo.repository;

import com.emobile.springtodo.entity.User;
import com.emobile.springtodo.exception.EntityNotFoundException;
import com.emobile.springtodo.repository.mapper.TaskMapper;
import com.emobile.springtodo.repository.mapper.UserMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("Тесты UserRepository")
@Testcontainers
@DataJpaTest
@Import({UserRepository.class, UserMapper.class, TaskRepository.class, TaskMapper.class})
public class UserRepositoryTest {

    @Container
    @ServiceConnection
    public static PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:16");

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Test
    @Sql("/sql/init-users.sql")
    @DisplayName("findById() возвращает корректного пользователя если он существует")
    void findById_shouldReturnUser_WhenUserExists() {
        User user = userRepository.findById(1L);

        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getUsername()).isEqualTo("User1");
    }

    @Test
    @DisplayName("findById() генерирует исключение, если пользователь с указанным id не найден")
    void findById_shouldThrowException_whenUserNotExist() {
        assertThrows(EntityNotFoundException.class, () -> userRepository.findById(1L));
    }

    @Test
    @Sql("/sql/init-users.sql")
    @DisplayName("findAll() возвращает список пользователей")
    void findAll_shouldReturnListOfUser_whenUsersExist() {
        List<User> users = userRepository.findAll(2, 0);
        assertThat(users).hasSize(2);
        assertThat(users.getFirst().getUsername()).isEqualTo("User1");
        assertThat(users.get(1).getUsername()).isEqualTo("User2");
    }

    @Test
    @DisplayName("findAll() генерирует исключение если пользователи не найдены")
    void findAll_shouldThrow_WhenUsersNotExist() {
        assertThrows(EntityNotFoundException.class, () -> userRepository.findAll(0, 10));
    }

    @Test
    @DisplayName("save() сохраняет пользователя")
    void save_shouldSaveUser() {
        User newUser = createTestUser();

        userRepository.save(newUser);

        List<User> createdUser = userRepository.findAll(10, 0);
        assertThat(createdUser).hasSize(1);
        assertThat(createdUser.getFirst().getUsername()).isEqualTo("TestUser");
    }

    @Test
    @Sql("/sql/init-users.sql")
    @DisplayName("update() обновляет пользователя")
    void update_shouldUpdateUser() {
        User user = userRepository.findById(1L);
        user.setUsername("TestUser");

        userRepository.update(user);

        User updatedUser = userRepository.findById(1L);
        assertThat(updatedUser.getUsername()).isEqualTo("TestUser");
    }

    @Test
    @Sql("/sql/init-users.sql")
    @DisplayName("existsById() возвращает true если пользователь существует")
    void existsById_shouldReturnTrue_whenUserExists() {
        assertThat(userRepository.existsById(1L)).isTrue();
    }

    @Test
    @DisplayName("existsById() возвращает false если пользователь не существует")
    void existsById_shouldReturnFalse_whenUserNotExist() {
        assertThat(userRepository.existsById(1L)).isFalse();
    }

    @Test
    @Sql({"/sql/init-users.sql", "/sql/init-tasks.sql"})
    @DisplayName("delete() корректно удаляет пользователя и связанные с ним дела")
    void delete_shouldDeleteUserAndTasksForThisUser() {
        assertThat(userRepository.existsById(1L)).isTrue();

        userRepository.delete(1L);

        assertThat(userRepository.existsById(1L)).isFalse();
        assertThrows(EntityNotFoundException.class, () -> taskRepository.findAllForUser(1L, 0, 10));
    }

    @Test
    @Sql("/sql/init-users.sql")
    @DisplayName("findByUsername() возвращает пользователя если пользователь с таким именем существует")
    void findByUsername_shouldReturnUser_whenUserExists() {
        User user = userRepository.findByUsername("User1");
        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getUsername()).isEqualTo("User1");
    }

    @Test
    @DisplayName("findByUsername() возвращает null если пользователь с таким именем не существует")
    void findByUsername_shouldReturnNull_whenUserNotExist() {
        User user = userRepository.findByUsername("User1");
        assertNull(user);
    }

    private User createTestUser() {
        User user = new User();
        user.setUsername("TestUser");
        return user;
    }
}
