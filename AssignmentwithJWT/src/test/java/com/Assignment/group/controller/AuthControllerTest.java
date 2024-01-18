package com.Assignment.group.controller;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.security.Principal;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.Assignment.group.config.JwtUtils;
import com.Assignment.group.entity.JwtRequest;
import com.Assignment.group.entity.JwtResponse;
import com.Assignment.group.entity.User;
import com.Assignment.group.repository.UserRepository;
import com.Assignment.group.service.GroupUserDetailsService;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private GroupUserDetailsService groupUserDetailsservice;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private AuthController controller;
    
    @Mock
    private UserRepository userRepository;


    @Test
    public void testGenerateToken() throws Exception {
        // Mocking
        JwtRequest jwtRequest = new JwtRequest("testUser", "testPassword");
        UserDetails userDetails = mock(UserDetails.class);
        when(groupUserDetailsservice.loadUserByUsername("testUser")).thenReturn(userDetails);
        when(jwtUtils.generateToken(userDetails)).thenReturn("mockedToken");

        // Execution
        ResponseEntity<?> responseEntity = controller.generateToken(jwtRequest);

        // Assertions
        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertTrue(responseEntity.getBody() instanceof JwtResponse);
        JwtResponse jwtResponse = (JwtResponse) responseEntity.getBody();
        assertEquals("mockedToken", jwtResponse.getToken());
    }

    @Test
    public void testGenerateTokenUserNotFound() throws Exception {
        // Mocking
        JwtRequest jwtRequest = new JwtRequest("nonexistentUser", "testPassword");
        when(groupUserDetailsservice.loadUserByUsername("nonexistentUser"))
                .thenThrow(new UsernameNotFoundException("User not found"));

        // Execution and Assertions
        assertThrows(Exception.class, () -> {
            controller.generateToken(jwtRequest);
        }, "User not found");
    }

    @Test
    public void testAuthenticate() throws Exception {
        // Mocking
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
        .thenReturn(mock(Authentication.class));
        // Execution
        controller.authenticate("testUser", "testPassword");

        // No exception should be thrown
    }

    @Test
    public void testAuthenticateDisabledException() {
        // Mocking
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new DisabledException("User disabled"));

        // Execution and Assertions
        assertThrows(Exception.class, () -> {
            controller.authenticate("testUser", "testPassword");
        }, "USER DISABLED User disabled");
    }

    @Test
    public void testAuthenticateBadCredentialsException() {
        // Mocking
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        // Execution and Assertions
        assertThrows(Exception.class, () -> {
            controller.authenticate("testUser", "testPassword");
        }, "INVALID CREDENTIALS Invalid credentials");
    }
    @Test
    public void testGetCurrentUser_UserFound() {
        // Mocking repository response for an existing user
        User mockUser = new User();
        mockUser.setUserName("existingUser");
        mockUser.setPassword("password");
        mockUser.isActive(true);
        mockUser.setRoles("ROLE_USER");

        Principal mockPrincipal = Mockito.mock(Principal.class);
        when(mockPrincipal.getName()).thenReturn("existingUser");

        when(userRepository.findByUserName(mockUser.getUserName())).thenReturn(Optional.of(mockUser));

        // Call the method
        User user = controller.getCurrentUser(mockPrincipal);

        // Assertions
        assertEquals(mockUser, user);
    }

    @Test
    public void testGetCurrentUser_UserNotFound() {
        Principal mockPrincipal = Mockito.mock(Principal.class);
        when(mockPrincipal.getName()).thenReturn("nonExistingUser");

        when(userRepository.findByUserName("nonExistingUser")).thenReturn(Optional.empty());

        // Call the method and expect custom UsernameNotFoundException
        AuthController.UsernameNotFoundException thrown = assertThrows(AuthController.UsernameNotFoundException.class, () ->
               controller.getCurrentUser(mockPrincipal));

        // Assertions
        assertEquals("No user found for nonExistingUser.", thrown.getMessage());
    }



}
