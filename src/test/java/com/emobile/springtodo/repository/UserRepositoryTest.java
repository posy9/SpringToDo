package com.emobile.springtodo.repository;

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
@DisplayName("Тесты UserRepository")
@Testcontainers
@DataJpaTest
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
        User user = userRepository.findById(1L).get();

        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getUsername()).isEqualTo("User1");
    }

    @Test
    @Sql("/sql/init-users.sql")
    @DisplayName("findAll() возвращает список пользователей")
    void findAll_shouldReturnListOfUser_whenUsersExist() {
        List<User> users = userRepository.findAll(PageRequest.of(0, 2)).getContent();
        assertThat(users).hasSize(2);
        assertThat(users.getFirst().getUsername()).isEqualTo("User1");
        assertThat(users.get(1).getUsername()).isEqualTo("User2");
    }


    @Test
    @DisplayName("save() сохраняет пользователя")
    void save_shouldSaveUser() {
        User newUser = createTestUser();

        userRepository.save(newUser);

        List<User> createdUser = userRepository.findAll(PageRequest.of(0, 10)).getContent();
        assertThat(createdUser).hasSize(1);
        assertThat(createdUser.getFirst().getUsername()).isEqualTo("TestUser");
    }

    @Test
    @Sql("/sql/init-users.sql")
    @DisplayName("update() обновляет пользователя")
    void update_shouldUpdateUser() {
        User user = userRepository.findById(1L).get();
        user.setUsername("TestUser");

        userRepository.save(user);

        User updatedUser = userRepository.findById(1L).get();
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

        userRepository.deleteById(1L);

        assertThat(userRepository.existsById(1L)).isFalse();
    }

    @Test
    @Sql("/sql/init-users.sql")
    @DisplayName("findByUsername() возвращает пользователя если пользователь с таким именем существует")
    void findByUsername_shouldReturnUser_whenUserExists() {
        User user = userRepository.findByUsername("User1").get();
        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getUsername()).isEqualTo("User1");
    }

    private User createTestUser() {
        User user = new User();
        user.setUsername("TestUser");
        return user;
    }
}
