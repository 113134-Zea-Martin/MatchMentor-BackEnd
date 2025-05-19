package com.scaffold.template.services;

import com.scaffold.template.entities.PasswordResetToken;
import com.scaffold.template.entities.UserEntity;
import com.scaffold.template.repositories.PasswordResetTokenRepository;
import com.scaffold.template.repositories.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PasswordResetTokenServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private PasswordResetTokenService passwordResetTokenService;

    private UserEntity userEntity;
    private PasswordResetToken validToken;
    private PasswordResetToken expiredToken;
    private PasswordResetToken usedToken;
    private String tokenString;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(passwordResetTokenService, "secretKey", "testsecretkeyforjwtgenerationthatislongenough");
        ReflectionTestUtils.setField(passwordResetTokenService, "expirationTimeInHours", 1);

        userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setEmail("test@example.com");
        userEntity.setPassword("oldPassword");

        tokenString = "valid-token";

        validToken = new PasswordResetToken();
        validToken.setToken(tokenString);
        validToken.setUser(userEntity);
        validToken.setExpiryDate(LocalDateTime.now().plusMinutes(30));
        validToken.setUsed(false);

        expiredToken = new PasswordResetToken();
        expiredToken.setToken("expired-token");
        expiredToken.setUser(userEntity);
        expiredToken.setExpiryDate(LocalDateTime.now().minusMinutes(30));
        expiredToken.setUsed(false);

        usedToken = new PasswordResetToken();
        usedToken.setToken("used-token");
        usedToken.setUser(userEntity);
        usedToken.setExpiryDate(LocalDateTime.now().plusMinutes(30));
        usedToken.setUsed(true);
    }

    @Test
    void createTokenForUserShouldDeleteExistingTokensAndCreateNewOne() {
        when(passwordResetTokenRepository.save(any(PasswordResetToken.class))).thenReturn(validToken);

        String token = passwordResetTokenService.createTokenForUser(userEntity);

        verify(passwordResetTokenRepository).deleteByUser(userEntity.getId());
        verify(passwordResetTokenRepository).save(any(PasswordResetToken.class));
        assertNotNull(token);
    }

    @Test
    void validateTokenShouldReturnFalseWhenTokenNotFound() {
        when(passwordResetTokenRepository.findByToken(anyString())).thenReturn(Optional.empty());

        boolean result = passwordResetTokenService.validateToken("non-existent-token");

        assertFalse(result);
    }

    @Test
    void validateTokenShouldReturnFalseWhenTokenIsUsed() {
        when(passwordResetTokenRepository.findByToken("used-token")).thenReturn(Optional.of(usedToken));

        boolean result = passwordResetTokenService.validateToken("used-token");

        assertFalse(result);
    }

    @Test
    void validateTokenShouldReturnFalseWhenTokenIsExpired() {
        when(passwordResetTokenRepository.findByToken("expired-token")).thenReturn(Optional.of(expiredToken));

        boolean result = passwordResetTokenService.validateToken("expired-token");

        assertFalse(result);
    }

    @Test
    void validateTokenShouldReturnTrueForValidToken() {
        // Crear un token JWT válido
        String validJwtToken = Jwts.builder()
                .setSubject(userEntity.getEmail())
                .setIssuedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(Date.from(LocalDateTime.now().plusHours(1).atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(SignatureAlgorithm.HS256, "testsecretkeyforjwtgenerationthatislongenough")
                .compact();

        validToken.setToken(validJwtToken);

        when(passwordResetTokenRepository.findByToken(validJwtToken)).thenReturn(Optional.of(validToken));

        boolean result = passwordResetTokenService.validateToken(validJwtToken);

        assertTrue(result);
    }

    @Test
    void resetPasswordShouldReturnFalseWhenTokenNotFound() {
        when(passwordResetTokenRepository.findByToken(anyString())).thenReturn(Optional.empty());

        boolean result = passwordResetTokenService.resetPassword("non-existent-token", "newPassword");

        assertFalse(result);
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(UserEntity.class));
    }

    @Test
    void resetPasswordShouldReturnFalseWhenTokenIsInvalid() {
        when(passwordResetTokenRepository.findByToken("invalid-token")).thenReturn(Optional.of(expiredToken));

        boolean result = passwordResetTokenService.resetPassword("invalid-token", "newPassword");

        assertFalse(result);
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(UserEntity.class));
    }

    @Test
    void resetPasswordShouldUpdatePasswordAndMarkTokenAsUsedWhenValid() {
        // Crear un token JWT válido
        String validJwtToken = Jwts.builder()
                .setSubject(userEntity.getEmail())
                .setIssuedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(Date.from(LocalDateTime.now().plusHours(1).atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(SignatureAlgorithm.HS256, "testsecretkeyforjwtgenerationthatislongenough")
                .compact();

        validToken.setToken(validJwtToken);

        when(passwordResetTokenRepository.findByToken(validJwtToken)).thenReturn(Optional.of(validToken));
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");

        boolean result = passwordResetTokenService.resetPassword(validJwtToken, "newPassword");

        assertTrue(result);

        ArgumentCaptor<UserEntity> userCaptor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userRepository).save(userCaptor.capture());
        assertEquals("encodedNewPassword", userCaptor.getValue().getPassword());

        ArgumentCaptor<PasswordResetToken> tokenCaptor = ArgumentCaptor.forClass(PasswordResetToken.class);
        verify(passwordResetTokenRepository).save(tokenCaptor.capture());
        assertTrue(tokenCaptor.getValue().isUsed());
    }
}