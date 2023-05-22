package com.recipes.controllers;

import com.recipes.model.User;
import com.recipes.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UserControllerTest {
    @Mock
    private UserService userService;

    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userController = new UserController(userService);
    }

    @Test
    void testCreateUser() {
        // Arrange
        User user = new User();
        when(userService.createUser(user)).thenReturn(user);

        // Act
        User result = userController.createUser(user);

        // Assert
        assertEquals(user, result);
        verify(userService, times(1)).createUser(user);
    }

    @Test
    void testGetUserById() {
        // Arrange
        Long userId = 1L;
        User user = new User();
        when(userService.getUserByUserId(userId)).thenReturn(Optional.of(user));

        // Act
        User result = userController.getUserById(userId);

        // Assert
        assertEquals(user, result);
        verify(userService, times(1)).getUserByUserId(userId);
    }

    @Test
    void testGetUserById_UserNotFound() {
        // Arrange
        Long userId = 1L;
        when(userService.getUserByUserId(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> userController.getUserById(userId));
        verify(userService, times(1)).getUserByUserId(userId);
    }

    @Test
    void testGetAllUsers() {
        // Arrange
        List<User> users = new ArrayList<>();
        when(userService.getAllUsers()).thenReturn(users);

        // Act
        List<User> result = userController.getAllUsers();

        // Assert
        assertEquals(users, result);
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void testUpdateUser() {
        // Arrange
        String name = "John";
        User user = new User();

        // Act
        userController.updateUser(name, user);

        // Assert
        verify(userService, times(1)).updateUser(name, user);
    }

    @Test
    void testDeleteUser() {
        // Arrange
        Long userId = 1L;

        // Act
        userController.deleteUser(userId);

        // Assert
        verify(userService, times(1)).deleteUser(userId);
    }
}
