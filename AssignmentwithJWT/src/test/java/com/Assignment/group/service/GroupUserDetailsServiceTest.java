package com.Assignment.group.service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.Assignment.group.entity.User;
import com.Assignment.group.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GroupUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private GroupUserDetailsService userDetailsService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testLoadUserByUsername_UserFound() {
        // Mocking repository response for an existing user
        User mockUser = new User();
        mockUser.setUserName("existingUser");
        mockUser.setPassword("password");
        mockUser.isActive(true);
        mockUser.setRoles("ROLE_USER");

        when(userRepository.findByUserName(mockUser.getUserName())).thenReturn(Optional.of(mockUser));

        // Call the method
        UserDetails userDetails = userDetailsService.loadUserByUsername("existingUser");

        // Assertions
        assertEquals("existingUser", userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());
        // Add more assertions based on your implementation
    }

    @Test
    public void testLoadUserByUsername_UserNotFound() {
        // Mocking repository response for a non-existing user
        Mockito.when(userRepository.findByUserName("nonExistingUser")).thenReturn(Optional.empty());

        // Call the method and expect UsernameNotFoundException
        assertThrows(UsernameNotFoundException.class, () ->
                userDetailsService.loadUserByUsername("nonExistingUser"));
    }
}

