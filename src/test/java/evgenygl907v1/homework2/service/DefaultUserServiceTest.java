package evgenygl907v1.homework2.service;

import evgenygl907v1.homework2.dao.UserDao;
import evgenygl907v1.homework2.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultUserServiceTest {
    @Mock
    private UserDao userDao;

    @InjectMocks
    private DefaultUserService userService;

    @Test
    void createUser_ShouldSaveUser_WhenDataIsValid() {
        String name = "testName1";
        String email = "test@test.com";
        String age = "25";

        userService.createUser(name, email, age);

        verify(userDao, times(1)).save(any(User.class));
    }

    @Test
    void createUser_ShouldThrowException_WhenNameIsEmpty() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser("", "qwe@test.com", "20");
        });
        assertEquals("имя не может быть пустым", exception.getMessage());
        verifyNoInteractions(userDao);
    }

    @Test
    void createUser_ShouldThrowException_WhenNameIsTooLong() {
        String longName = "a".repeat(51);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser(longName, "qwe@test.com", "20");
        });
        assertEquals("имя длиннее 50 символов", exception.getMessage());
        verifyNoInteractions(userDao);
    }

    @Test
    void createUser_ShouldThrowException_WhenEmailIsInvalid() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser("qwe", "qwertqqer", "20");
        });
        assertEquals("некорректный email", exception.getMessage());
        verifyNoInteractions(userDao);
    }

    @Test
    void createUser_ShouldThrowException_WhenEmailIsTooLong() {
        String longEmail = "a".repeat(99) + "@b.com";
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser("qwe", longEmail, "20");
        });
        assertEquals("некорректный email", exception.getMessage());
        verifyNoInteractions(userDao);
    }

    @Test
    void createUser_ShouldThrowException_WhenAgeIsNotNumber() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser("qwe", "qwe@test.com", "abc");
        });
        assertEquals("возраст должен быть числом", exception.getMessage());
        verifyNoInteractions(userDao);
    }

    @Test
    void createUser_ShouldThrowException_WhenAgeIsInvalid() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser("qwe", "qwe@test.com", "0");
        });
        assertEquals("возраст должен быть от 1 до 130", exception.getMessage());
        verifyNoInteractions(userDao);
    }

    @Test
    void createUser_ShouldThrowException_WhenAgeIsTooHigh() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser("qwe", "qwe@test.com", "131");
        });
        assertEquals("возраст должен быть от 1 до 130", exception.getMessage());
        verifyNoInteractions(userDao);
    }


    @Test
    void findUser_ShouldCallDao_WhenIdIsValid() {
        when(userDao.findById(1L)).thenReturn(Optional.of(new User("Test", "test@test.com", 20)));

        userService.findUser("1");

        verify(userDao).findById(1L);
    }

    @Test
    void findUser_ShouldThrowException_WhenIdIsNotNumber() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.findUser("abc");
        });
        assertEquals("id должен быть числом", exception.getMessage());
        verifyNoInteractions(userDao);
    }

    @Test
    void findUser_ShouldThrowException_WhenIdIsNegative() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.findUser("-5");
        });
        assertEquals("id должен быть положительным", exception.getMessage());
        verifyNoInteractions(userDao);
    }

    @Test
    void listUsers_ShouldReturnListFromDao() {
        List<User> expectedUsers = List.of(new User("A", "a@test.com", 20));
        when(userDao.findAll()).thenReturn(expectedUsers);

        List<User> actualUsers = userService.listUsers();

        assertEquals(expectedUsers, actualUsers);
        verify(userDao).findAll();
    }

    @Test
    void listUsers_ShouldReturnEmptyList_WhenDaoReturnsEmpty() {
        when(userDao.findAll()).thenReturn(Collections.emptyList());

        List<User> actualUsers = userService.listUsers();

        assertTrue(actualUsers.isEmpty());
        verify(userDao).findAll();
    }

    @Test
    void updateUser_ShouldUpdateAndSave_WhenUserExists() {
        // Arrange
        User existingUser = new User("OldName", "old@test.com", 30);
        existingUser.setId(1L);
        when(userDao.findById(1L)).thenReturn(Optional.of(existingUser));

        when(userDao.update(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        userService.updateUser("1", "NewName", "new@test.com", "35");

        // Assert
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userDao).update(userCaptor.capture());

        User updatedUser = userCaptor.getValue();
        assertEquals("NewName", updatedUser.getName());
        assertEquals("new@test.com", updatedUser.getEmail());
        assertEquals(35, updatedUser.getAge());
    }

    @Test
    void updateUser_ShouldThrowException_WhenUserNotFound() {
        when(userDao.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.updateUser("1", "NewName", "new@test.com", "35");
        });

        assertEquals("Пользователь id=1 не найден", exception.getMessage());
        verify(userDao, never()).update(any(User.class));
    }

    @Test
    void updateUser_ShouldThrowException_WhenIdIsInvalid() {
        assertThrows(IllegalArgumentException.class, () -> {
            userService.updateUser("-1", "NewName", "new@test.com", "35");
        });
        verifyNoInteractions(userDao);
    }

    @Test
    void updateUser_ShouldThrowException_WhenNewNameIsInvalid() {
        User existingUser = new User("OldName", "old@test.com", 30);
        existingUser.setId(1L);
        when(userDao.findById(1L)).thenReturn(Optional.of(existingUser));

        assertThrows(IllegalArgumentException.class, () -> {
            userService.updateUser("1", "", "new@test.com", "35");
        });

        verify(userDao, never()).update(any(User.class));
    }

    @Test
    void deleteUser_ShouldCallDao_WhenIdIsValid() {
        when(userDao.deleteById(1L)).thenReturn(true);

        userService.deleteUser("1");

        verify(userDao).deleteById(1L);
    }

    @Test
    void deleteUser_ShouldThrowException_WhenIdIsInvalid() {
        assertThrows(IllegalArgumentException.class, () -> {
            userService.deleteUser("abc");
        });
        verifyNoInteractions(userDao);
    }
}