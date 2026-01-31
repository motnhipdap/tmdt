package com.dev.dungcony.modules.auth.services.impl;

import com.dev.dungcony.modules.auth.config.JwtConfig;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
@DisplayName("JwtService Unit Tests")
class JwtServiceImplTest {

    @Mock
    private JwtConfig jwtConfig;

    private JwtServiceImpl jwtService;

    @BeforeEach
    void setUp() {
        // Mock JWT configuration - Secret key must be >= 512 bits (64 bytes) for HS512
        lenient().when(jwtConfig.getSecret())
                .thenReturn("mySecretKeyForJWTTokenGenerationAndValidation1234567890123456789012345678901234567890");
        lenient().when(jwtConfig.getExpiration()).thenReturn(3600000L); // 1 hour

        jwtService = new JwtServiceImpl(jwtConfig);
    }

    @Test
    @DisplayName("Generate Token - With Username and Role")
    void generateToken_WithUsernameAndRole() {
        // Arrange
        int userId = 1;
        String username = "testuser";
        String role = "customer";

        // Act
        String token = jwtService.generateToken(userId, username, role);

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    @DisplayName("Generate Token - With Email")
    void generateToken_WithEmail() {
        // Arrange
        int userId = 1;
        String email = "test@example.com";

        // Act
        String token = jwtService.generateToken(userId, email);

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    @DisplayName("Extract Username - Success")
    void extractUsername_Success() {
        // Arrange
        int userId = 1;
        String username = "testuser";
        String role = "customer";
        String token = jwtService.generateToken(userId, username, role);

        // Act
        String extractedUsername = jwtService.extractUsername(token);

        // Assert
        assertEquals(username, extractedUsername);
    }

    @Test
    @DisplayName("Extract UserId - Success")
    void extractUserId_Success() {
        // Arrange
        int userId = 1;
        String username = "testuser";
        String role = "customer";
        String token = jwtService.generateToken(userId, username, role);

        // Act
        Integer extractedUserId = jwtService.extractUserId(token);

        // Assert
        assertEquals(userId, extractedUserId);
    }

    @Test
    @DisplayName("Extract Role - Success")
    void extractRole_Success() {
        // Arrange
        int userId = 1;
        String username = "testuser";
        String role = "customer";
        String token = jwtService.generateToken(userId, username, role);

        // Act
        String extractedRole = jwtService.extractRole(token);

        // Assert
        assertEquals(role, extractedRole);
    }

    @Test
    @DisplayName("Extract Email - Success")
    void extractEmail_Success() {
        // Arrange
        int userId = 1;
        String email = "test@example.com";
        String token = jwtService.generateToken(userId, email);

        // Act
        String extractedEmail = jwtService.extractEmail(token);

        // Assert
        assertEquals(email, extractedEmail);
    }

    @Test
    @DisplayName("Validate Token - Valid Token")
    void validateToken_ValidToken() {
        // Arrange
        int userId = 1;
        String username = "testuser";
        String role = "customer";
        String token = jwtService.generateToken(userId, username, role);

        // Act
        boolean isValid = jwtService.validateToken(token);

        // Assert
        assertTrue(isValid);
    }

    @Test
    @DisplayName("Validate Token - Invalid Token")
    void validateToken_InvalidToken() {
        // Arrange
        String invalidToken = "invalid.token.here";

        // Act
        boolean isValid = jwtService.validateToken(invalidToken);

        // Assert
        assertFalse(isValid);
    }

    @Test
    @DisplayName("Is Token Expired - Not Expired")
    void isTokenExpired_NotExpired() {
        // Arrange
        int userId = 1;
        String username = "testuser";
        String role = "customer";
        String token = jwtService.generateToken(userId, username, role);

        // Act
        boolean isExpired = jwtService.isTokenExpired(token);

        // Assert
        assertFalse(isExpired);
    }

    @Test
    @DisplayName("Extract All Claims - Success")
    void extractAllClaims_Success() {
        // Arrange
        int userId = 1;
        String username = "testuser";
        String role = "customer";
        String token = jwtService.generateToken(userId, username, role);

        // Act
        Claims claims = jwtService.extractAllClaims(token);

        // Assert
        assertNotNull(claims);
        assertEquals(String.valueOf(userId), claims.getSubject());
        assertEquals(username, claims.get("username", String.class));
        assertEquals(role, claims.get("role", String.class));
        assertNotNull(claims.getIssuedAt());
        assertNotNull(claims.getExpiration());
    }

    @Test
    @DisplayName("Extract Username - Invalid Token Returns Null")
    void extractUsername_InvalidToken_ReturnsNull() {
        // Arrange
        String invalidToken = "invalid.token.here";

        // Act
        String username = jwtService.extractUsername(invalidToken);

        // Assert
        assertNull(username);
    }

    @Test
    @DisplayName("Extract UserId - Invalid Token Returns Null")
    void extractUserId_InvalidToken_ReturnsNull() {
        // Arrange
        String invalidToken = "invalid.token.here";

        // Act
        Integer userId = jwtService.extractUserId(invalidToken);

        // Assert
        assertNull(userId);
    }
}
