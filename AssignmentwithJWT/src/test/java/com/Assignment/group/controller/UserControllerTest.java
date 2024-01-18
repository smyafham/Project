package com.Assignment.group.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.Assignment.group.common.UserConstant;
import com.Assignment.group.entity.User;
import com.Assignment.group.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserController userController;

    @Test
    void getAllUsers() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        List<User> result = userController.getAllUsers();

        verify(userRepository, times(1)).findAll();

        assertEquals(Collections.emptyList(), result);
    }

    @Test
    void getUserById() {
        int userId = 1;
        User user = new User();
        user.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        ResponseEntity<User> result = userController.getUserById(userId);

        verify(userRepository, times(1)).findById(userId);
        assertEquals(ResponseEntity.ok(user), result);
    }

    @Test
    void createUser_ValidUser_ReturnsSuccessMessage() throws Exception {
        // Arrange
        User newUser = new User();
        newUser.setUserName("newUser");
        newUser.setPassword("password");

        when(userRepository.findAll()).thenReturn(Collections.emptyList());
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        String response = userController.createUser(newUser);

        // Assert
        assertEquals("newUser has been created", response);
        verify(userRepository, times(1)).save(newUser);
    }

    @Test
    void createUser_ExistingUser_ThrowsException() {
        // Arrange
        User existingUser = new User();
        existingUser.setUserName("existingUser");
        existingUser.setPassword("password");

        when(userRepository.findAll()).thenReturn(List.of(existingUser));

        // Act and Assert
        assertThrows(Exception.class, () -> userController.createUser(existingUser));
        verify(userRepository, never()).save(existingUser);
    }

    @Test
    void createUser_NullUserName_ThrowsException() {
        // Arrange
        User userWithNullUserName = new User();
        userWithNullUserName.setPassword("password");

        // Act and Assert
        assertThrows(Exception.class, () -> userController.createUser(userWithNullUserName));
        verify(userRepository, never()).save(userWithNullUserName);
    }
//    @Test
//    void updateUser() {
//        int userId = 1;
//        User existingUser = new User();
//        existingUser.setId(userId);
//        existingUser.setUserName("oldName");
//        existingUser.setPassword("oldPassword");
//        existingUser.setRoles(UserConstant.DEFAULT_ROLE);
//
//        User userDetails = new User();
//        userDetails.setUserName("newName");
//        userDetails.setPassword("newPassword");
//        userDetails.setRoles(UserConstant.DEFAULT_ROLE);
//
//    //    when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
//
//        ResponseEntity<User> result = userController.updateUser(userId, userDetails);
//
////        verify(userRepository, times(1)).findById(userId);
////        verify(passwordEncoder, times(1)).encode("newPassword");
////        verify(userRepository, times(1)).save(existingUser);
//
//      //  assertEquals(ResponseEntity.ok(existingUser), result);
//      //  assertEquals("newName", existingUser.getUserName());
//        assertEquals(userDetails.getUserName(),result.getBody().getUserName());
//    }
    @Test
    void updateUser_ValidIdAndUserDetails_ReturnsOkResponse() {
        // Arrange
        Integer userId = 1;
        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setUserName("existingUser");
        existingUser.setPassword("existingPassword");
        existingUser.setRoles("ROLE_USER");
        existingUser.isActive(true);

        User updatedUserDetails = new User();
        updatedUserDetails.setId(userId);
        updatedUserDetails.setUserName("updatedUser");
        updatedUserDetails.setPassword("updatedPassword");
        updatedUserDetails.setRoles("ROLE_ADMIN");
        updatedUserDetails.isActive(false);

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.encode(updatedUserDetails.getPassword())).thenReturn("encryptedPassword");
        when(userRepository.save(any(User.class))).thenReturn(updatedUserDetails);

        // Act
        ResponseEntity<User> response = userController.updateUser(userId, updatedUserDetails);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedUserDetails, response.getBody());
    }

    @Test
    void updateUser_InvalidId_ReturnsNotFoundResponse() {
        // Arrange
        Integer userId = 1;
        User updatedUserDetails = new User();
        updatedUserDetails.setId(userId);
        updatedUserDetails.setUserName("updatedUser");
        updatedUserDetails.setPassword("updatedPassword");
        updatedUserDetails.setRoles("ROLE_ADMIN");
        updatedUserDetails.isActive(false);

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<User> response = userController.updateUser(userId, updatedUserDetails);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }
    @Test
    void deleteUser_ExistingUserId_ReturnsSuccessMessage() {
        // Arrange
        Integer userId = 1;
        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setUserName("existingUser");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

        // Act
        String response = userController.deleteUser(userId);

        // Assert
        assertEquals("existingUser has been deleted", response);
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void deleteUser_NonExistingUserId_ReturnsNotFoundMessage() {
        // Arrange
        Integer userId = 1;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act
        String response = userController.deleteUser(userId);

        // Assert
        assertEquals("User with ID 1 does not exist", response);
        verify(userRepository, never()).deleteById(userId);
    }

    @Test
    void deleteUser_ExceptionThrown_ReturnsErrorMessage() {
        // Arrange
        Integer userId = 1;

        when(userRepository.findById(userId)).thenThrow(new RuntimeException("Simulated exception"));

        // Act
        String response = userController.deleteUser(userId);

        // Assert
        assertEquals("An error occurred while deleting the user", response);
        verify(userRepository, never()).deleteById(userId);
    }

    @Test
    void getRolesByLoggedInUser() {
        Principal principal = mock(Principal.class);

        User superAdminUser = new User();
        superAdminUser.setRoles("SUPERADMIN,ADMIN");

        when(userRepository.findByUserName("superadmin")).thenReturn(Optional.of(superAdminUser));
        when(principal.getName()).thenReturn("superadmin");

        List<String> result = userController.getRolesByLoggedInUser(principal);

        assertEquals(Arrays.asList(UserConstant.SUPERADMIN_ACCESS), result);
    }

}