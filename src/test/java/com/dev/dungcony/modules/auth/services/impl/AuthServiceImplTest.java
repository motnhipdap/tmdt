package com.dev.dungcony.modules.auth.services.impl;

import com.dev.dungcony.modules.auth.config.JwtConfig;
import com.dev.dungcony.modules.auth.dtos.requests.RegisReq;
import com.dev.dungcony.modules.auth.dtos.responses.AccountRes;
import com.dev.dungcony.modules.auth.dtos.responses.LoginRes;
import com.dev.dungcony.modules.auth.dtos.responses.LoginResult;
import com.dev.dungcony.modules.auth.entities.Account;
import com.dev.dungcony.modules.auth.exceptions.EmailExistException;
import com.dev.dungcony.modules.auth.exceptions.InvalidCredentialsException;
import com.dev.dungcony.modules.auth.exceptions.UsernameExistsException;
import com.dev.dungcony.modules.auth.repositories.AccountRepository;
import com.dev.dungcony.modules.auth.services.interfaces.JwtService;
import com.dev.dungcony.modules.auth.services.interfaces.RefreshTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthService Unit Tests")
class AuthServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private JwtConfig jwtConfig;

    @Mock
    private RefreshTokenService refreshTokenService;

    @InjectMocks
    private AuthServiceImpl authService;

    private Account testAccount;
    private RegisReq testRegisReq;

    @BeforeEach
    void setUp() {
        testAccount = new Account();
        testAccount.setId(1);
        testAccount.setUsername("testuser");
        testAccount.setEmail("test@example.com");
        testAccount.setPassword("encodedPassword");
        testAccount.setRole("customer");
        testAccount.setStatus("active");
        testAccount.setCreatedAt(LocalDateTime.now());

        testRegisReq = new RegisReq("test@example.com", "testuser", "password123");
    }

    @Test
    @DisplayName("Register - Success")
    void register_Success() {
        // Arrange
        when(accountRepository.existsByEmail(anyString())).thenReturn(false);
        when(accountRepository.existsByUsername(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(accountRepository.save(any(Account.class))).thenReturn(testAccount);

        // Act
        assertDoesNotThrow(() -> authService.register(testRegisReq));

        // Assert
        verify(accountRepository, times(1)).existsByEmail(testRegisReq.email());
        verify(accountRepository, times(1)).existsByUsername(testRegisReq.username());
        verify(passwordEncoder, times(1)).encode(testRegisReq.password());
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    @DisplayName("Register - Email Already Exists")
    void register_EmailAlreadyExists() {
        // Arrange
        when(accountRepository.existsByEmail(anyString())).thenReturn(true);

        // Act & Assert
        assertThrows(EmailExistException.class, () -> authService.register(testRegisReq));
        verify(accountRepository, times(1)).existsByEmail(testRegisReq.email());
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    @DisplayName("Register - Username Already Exists")
    void register_UsernameAlreadyExists() {
        // Arrange
        when(accountRepository.existsByEmail(anyString())).thenReturn(false);
        when(accountRepository.existsByUsername(anyString())).thenReturn(true);

        // Act & Assert
        assertThrows(UsernameExistsException.class, () -> authService.register(testRegisReq));
        verify(accountRepository, times(1)).existsByUsername(testRegisReq.username());
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    @DisplayName("Register - DataIntegrityViolationException")
    void register_DataIntegrityViolation() {
        // Arrange
        when(accountRepository.existsByEmail(anyString())).thenReturn(false);
        when(accountRepository.existsByUsername(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(accountRepository.save(any(Account.class))).thenThrow(DataIntegrityViolationException.class);

        // Act & Assert
        assertThrows(EmailExistException.class, () -> authService.register(testRegisReq));
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    @DisplayName("Login - Success")
    void login_Success() {
        // Arrange
        String username = "testuser";
        String password = "password123";
        String token = "jwt.token.here";
        String refreshToken = "refresh.token.here";
        long expiration = 3600000L;

        when(accountRepository.findByUsername(username)).thenReturn(Optional.of(testAccount));
        when(passwordEncoder.matches(password, testAccount.getPassword())).thenReturn(true);
        when(jwtService.generateToken(anyInt(), anyString(), anyString())).thenReturn(token);
        when(refreshTokenService.create(anyInt())).thenReturn(refreshToken);
        when(jwtConfig.getExpiration()).thenReturn(expiration);
        when(jwtConfig.getRefreshExpiration()).thenReturn(expiration * 2);

        // Act
        LoginResult result = authService.login(username, password);

        // Assert
        assertNotNull(result);
        assertEquals(token, result.token());
        assertNotNull(result.refreshToken());
        verify(accountRepository, times(1)).findByUsername(username);
        verify(passwordEncoder, times(1)).matches(password, testAccount.getPassword());
        verify(jwtService, times(1)).generateToken(testAccount.getId(), testAccount.getUsername(), testAccount.getRole());
        verify(refreshTokenService, times(1)).create(testAccount.getId());
    }

    @Test
    @DisplayName("Login - Invalid Username")
    void login_InvalidUsername() {
        // Arrange
        String username = "nonexistent";
        String password = "password123";

        when(accountRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(InvalidCredentialsException.class, () -> authService.login(username, password));
        verify(accountRepository, times(1)).findByUsername(username);
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    @Test
    @DisplayName("Login - Invalid Password")
    void login_InvalidPassword() {
        // Arrange
        String username = "testuser";
        String password = "wrongpassword";

        when(accountRepository.findByUsername(username)).thenReturn(Optional.of(testAccount));
        when(passwordEncoder.matches(password, testAccount.getPassword())).thenReturn(false);

        // Act & Assert
        assertThrows(InvalidCredentialsException.class, () -> authService.login(username, password));
        verify(accountRepository, times(1)).findByUsername(username);
        verify(passwordEncoder, times(1)).matches(password, testAccount.getPassword());
        verify(jwtService, never()).generateToken(anyInt(), anyString(), anyString());
    }

    @Test
    @DisplayName("Refresh Token - Success")
    void refreshToken_Success() {
        // Arrange
        String refreshToken = "refresh.token.here";
        String newToken = "new.jwt.token.here";
        AccountRes accountRes = new AccountRes(
                1,
                "test@example.com",
                "testuser",
                null,
                "active",
                "customer",
                LocalDateTime.now()
        );

        when(refreshTokenService.verify(refreshToken)).thenReturn(accountRes);
        when(jwtService.generateToken(accountRes.id(), accountRes.username(), accountRes.role())).thenReturn(newToken);

        // Act
        LoginRes result = authService.refreshToken(refreshToken);

        // Assert
        assertNotNull(result);
        assertEquals(newToken, result.token());
        verify(refreshTokenService, times(1)).verify(refreshToken);
        verify(jwtService, times(1)).generateToken(accountRes.id(), accountRes.username(), accountRes.role());
    }

    @Test
    @DisplayName("Logout - Success")
    void logout_Success() {
        // Arrange
        String refreshToken = "refresh.token.here";
        doNothing().when(refreshTokenService).revoke(refreshToken);

        // Act
        assertDoesNotThrow(() -> authService.logout(refreshToken));

        // Assert
        verify(refreshTokenService, times(1)).revoke(refreshToken);
    }
}
