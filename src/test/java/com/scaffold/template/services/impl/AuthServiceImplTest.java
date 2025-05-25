package com.scaffold.template.services.impl;

import com.scaffold.template.dtos.auth.UserRegisterRequestDTO;
import com.scaffold.template.dtos.auth.login.LoginRequestDTO;
import com.scaffold.template.dtos.auth.login.LoginResponseDTO;
import com.scaffold.template.dtos.profile.UserResponseDTO;
import com.scaffold.template.entities.Role;
import com.scaffold.template.entities.UserActivityEntity;
import com.scaffold.template.entities.UserEntity;
import com.scaffold.template.repositories.UserRepository;
import com.scaffold.template.security.JwtConfig;
import com.scaffold.template.services.userActivity.UserActivityService;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthServiceImplTest {

    @Test
    void getAllUsersReturnsEmptyListWhenNoUsersExist() {
        UserRepository mockUserRepository = mock(UserRepository.class);
        when(mockUserRepository.findAll()).thenReturn(new ArrayList<>());

        AuthServiceImpl authService = new AuthServiceImpl(mockUserRepository, null, null, null, null);

        List<UserResponseDTO> result = authService.getAllUsers();

        assertTrue(result.isEmpty());
    }

    @Test
    void registerUserThrowsExceptionWhenEmailAlreadyExists() {
        UserRepository mockUserRepository = mock(UserRepository.class);
        when(mockUserRepository.existsByEmail("existing@example.com")).thenReturn(true);

        AuthServiceImpl authService = new AuthServiceImpl(mockUserRepository, null, null, null, null);

        UserRegisterRequestDTO request = new UserRegisterRequestDTO();
        request.setEmail("existing@example.com");

        assertThrows(IllegalArgumentException.class, () -> authService.registerUser(request));
    }

    @Test
    void authenticateUserThrowsExceptionWhenUserNotFound() {
        UserRepository mockUserRepository = mock(UserRepository.class);
        when(mockUserRepository.findByEmail("nonexistent@example.com")).thenReturn(null);

        AuthServiceImpl authService = new AuthServiceImpl(mockUserRepository, null, null, null, null);

        assertThrows(IllegalArgumentException.class, () -> authService.authenticateUser("nonexistent@example.com", "password"));
    }

    @Test
    void loginThrowsExceptionWhenAuthenticationFails() {
        AuthenticationManager mockAuthManager = mock(AuthenticationManager.class);
        when(mockAuthManager.authenticate(any())).thenThrow(new RuntimeException("Authentication failed"));

        AuthServiceImpl authService = new AuthServiceImpl(null, null, mockAuthManager, null, null);

        LoginRequestDTO loginRequest = new LoginRequestDTO();
        loginRequest.setEmail("user@example.com");
        loginRequest.setPassword("wrongpassword");

        assertThrows(RuntimeException.class, () -> authService.login(loginRequest));
    }

    @Test
    void getUserByEmailThrowsExceptionWhenUserNotFound() {
        UserRepository mockUserRepository = mock(UserRepository.class);
        when(mockUserRepository.findByEmail("nonexistent@example.com")).thenReturn(null);

        AuthServiceImpl authService = new AuthServiceImpl(mockUserRepository, null, null, null, null);

        assertThrows(IllegalArgumentException.class, () -> authService.getUserByEmail("nonexistent@example.com"));
    }

    @Test
    void authenticateUserThrowsExceptionWhenPasswordIsIncorrect() {
        UserRepository mockUserRepository = mock(UserRepository.class);
        PasswordEncoder mockPasswordEncoder = mock(PasswordEncoder.class);

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail("user@example.com");
        userEntity.setPassword("encodedPassword");
        userEntity.setIsActive(true);

        when(mockUserRepository.findByEmail("user@example.com")).thenReturn(userEntity);
        when(mockPasswordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

        AuthServiceImpl authService = new AuthServiceImpl(mockUserRepository, mockPasswordEncoder, null, null, null);

        assertThrows(IllegalArgumentException.class, () -> authService.authenticateUser("user@example.com", "wrongPassword"));
    }

    @Test
    void authenticateUserReturnsUserWhenCredentialsAreValid() {
        UserRepository mockUserRepository = mock(UserRepository.class);
        PasswordEncoder mockPasswordEncoder = mock(PasswordEncoder.class);

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail("user@example.com");
        userEntity.setPassword("encodedPassword");
        userEntity.setIsActive(true);

        when(mockUserRepository.findByEmail("user@example.com")).thenReturn(userEntity);
        when(mockPasswordEncoder.matches("correctPassword", "encodedPassword")).thenReturn(true);

        AuthServiceImpl authService = new AuthServiceImpl(mockUserRepository, mockPasswordEncoder, null, null, null);

        UserEntity result = authService.authenticateUser("user@example.com", "correctPassword");

        assertNotNull(result);
        assertEquals("user@example.com", result.getEmail());
    }

    @Test
    void loginReturnsTokenWhenAuthenticationSucceeds() {
        UserRepository mockUserRepository = mock(UserRepository.class);
        UserActivityService mockUserActivityService = mock(UserActivityService.class);
        AuthenticationManager mockAuthManager = mock(AuthenticationManager.class);
        JwtConfig mockJwtConfig = mock(JwtConfig.class);

//        AuthServiceImpl authService = new AuthServiceImpl(mockUserRepository, null, mockAuthManager, mockJwtConfig, mockUserActivityService);


        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setFirstName("John");
        userEntity.setLastName("Doe");
        userEntity.setEmail("user@example.com");
        userEntity.setRole(Role.STUDENT);
        userEntity.setIsActive(true);

        Authentication mockAuthentication = mock(Authentication.class);
        User mockUserDetails = mock(User.class);

        when(mockAuthManager.authenticate(any())).thenReturn(mockAuthentication);
        when(mockAuthentication.getPrincipal()).thenReturn(mockUserDetails);
        when(mockUserRepository.findByEmail("user@example.com")).thenReturn(userEntity);
        when(mockJwtConfig.generateToken(anyMap(), any())).thenReturn("mockToken");
        doNothing().when(mockUserActivityService).createUserActivity(anyLong(), anyString());

        AuthServiceImpl authService = new AuthServiceImpl(mockUserRepository, null, mockAuthManager, mockJwtConfig, mockUserActivityService);

        LoginRequestDTO loginRequest = new LoginRequestDTO();
        loginRequest.setEmail("user@example.com");
        loginRequest.setPassword("correctPassword");

        LoginResponseDTO result = authService.login(loginRequest);

        assertNotNull(result);
        assertEquals("mockToken", result.getToken());
        assertEquals("user@example.com", result.getEmail());
    }

    @Test
    void registerUserCreatesNewUserWhenEmailDoesNotExist() {
        UserRepository mockUserRepository = mock(UserRepository.class);
        PasswordEncoder mockPasswordEncoder = mock(PasswordEncoder.class);

        when(mockUserRepository.existsByEmail("newuser@example.com")).thenReturn(false);
        when(mockPasswordEncoder.encode("password")).thenReturn("encodedPassword");
        when(mockUserRepository.save(any(UserEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AuthServiceImpl authService = new AuthServiceImpl(mockUserRepository, mockPasswordEncoder, null, null, null);

        UserRegisterRequestDTO request = new UserRegisterRequestDTO();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setEmail("newuser@example.com");
        request.setPassword("password");

        UserEntity result = authService.registerUser(request);

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("newuser@example.com", result.getEmail());
        assertEquals("encodedPassword", result.getPassword());
        assertTrue(result.getIsActive());
        assertEquals(Role.STUDENT, result.getRole());
    }

    @Test
    void registerUserThrowsExceptionWhenPasswordIsNull() {
        UserRepository mockUserRepository = mock(UserRepository.class);

        when(mockUserRepository.existsByEmail("newuser@example.com")).thenReturn(false);

        AuthServiceImpl authService = new AuthServiceImpl(mockUserRepository, null, null, null, null);

        UserRegisterRequestDTO request = new UserRegisterRequestDTO();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setEmail("newuser@example.com");
        request.setPassword(null);

        assertThrows(IllegalArgumentException.class, () -> authService.registerUser(request));
    }

}