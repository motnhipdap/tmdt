package com.dev.dungcony.modules.auth.services.impl;

import com.dev.dungcony.modules.auth.dtos.res.AccountRes;
import com.dev.dungcony.modules.auth.entities.Account;
import com.dev.dungcony.modules.auth.exceptions.TokenValidException;
import com.dev.dungcony.modules.auth.repositories.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AccountService Unit Tests")
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AccountServiceImpl accountService;

    private Account testAccount;

    @BeforeEach
    void setUp() {
        testAccount = new Account();
        testAccount.setId(1);
        testAccount.setUsername("testuser");
        testAccount.setEmail("test@example.com");
        testAccount.setPassword("encodedPassword");
        testAccount.setPhone("0123456789");
        testAccount.setRole("customer");
        testAccount.setStatus("active");
        testAccount.setCreatedAt(LocalDateTime.now());
    }

    @Test
    @DisplayName("ExistsByEmail - Email Exists")
    void existsByEmail_EmailExists() {
        // Arrange
        String email = "test@example.com";
        when(accountRepository.existsByEmail(email)).thenReturn(true);

        // Act
        boolean result = accountService.existsByEmail(email);

        // Assert
        assertTrue(result);
        verify(accountRepository, times(1)).existsByEmail(email);
    }

    @Test
    @DisplayName("ExistsByEmail - Email Does Not Exist")
    void existsByEmail_EmailDoesNotExist() {
        // Arrange
        String email = "nonexistent@example.com";
        when(accountRepository.existsByEmail(email)).thenReturn(false);

        // Act
        boolean result = accountService.existsByEmail(email);

        // Assert
        assertFalse(result);
        verify(accountRepository, times(1)).existsByEmail(email);
    }

    @Test
    @DisplayName("ExistsByUsername - Username Exists")
    void existsByUsername_UsernameExists() {
        // Arrange
        String username = "testuser";
        when(accountRepository.existsByUsername(username)).thenReturn(true);

        // Act
        boolean result = accountService.existsByUsername(username);

        // Assert
        assertTrue(result);
        verify(accountRepository, times(1)).existsByUsername(username);
    }

    @Test
    @DisplayName("ExistsByUsername - Username Does Not Exist")
    void existsByUsername_UsernameDoesNotExist() {
        // Arrange
        String username = "nonexistent";
        when(accountRepository.existsByUsername(username)).thenReturn(false);

        // Act
        boolean result = accountService.existsByUsername(username);

        // Assert
        assertFalse(result);
        verify(accountRepository, times(1)).existsByUsername(username);
    }

    @Test
    @DisplayName("UpdatePassword - Success")
    void updatePassword_Success() {
        // Arrange
        int id = 1;
        String oldPassword = "oldPassword123";
        String newPassword = "newPassword123";
        String encodedNewPassword = "encodedNewPassword";
        String originalEncodedPassword = testAccount.getPassword(); // Capture original password

        when(accountRepository.findById(id)).thenReturn(Optional.of(testAccount));
        when(passwordEncoder.matches(oldPassword, originalEncodedPassword)).thenReturn(true);
        when(passwordEncoder.encode(newPassword)).thenReturn(encodedNewPassword);
        when(accountRepository.save(any(Account.class))).thenReturn(testAccount);

        // Act
        boolean result = accountService.updatePassword(id, oldPassword, newPassword);

        // Assert
        assertTrue(result);
        verify(accountRepository, times(1)).findById(id);
        verify(passwordEncoder, times(1)).matches(oldPassword, originalEncodedPassword);
        verify(passwordEncoder, times(1)).encode(newPassword);
        verify(accountRepository, times(1)).save(testAccount);
    }

    @Test
    @DisplayName("UpdatePassword - Wrong Old Password")
    void updatePassword_WrongOldPassword() {
        // Arrange
        int id = 1;
        String oldPassword = "wrongOldPassword";
        String newPassword = "newPassword123";
        String originalEncodedPassword = testAccount.getPassword(); // Capture original password

        when(accountRepository.findById(id)).thenReturn(Optional.of(testAccount));
        when(passwordEncoder.matches(oldPassword, originalEncodedPassword)).thenReturn(false);

        // Act
        boolean result = accountService.updatePassword(id, oldPassword, newPassword);

        // Assert
        assertFalse(result);
        verify(accountRepository, times(1)).findById(id);
        verify(passwordEncoder, times(1)).matches(oldPassword, originalEncodedPassword);
        verify(passwordEncoder, never()).encode(anyString());
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    @DisplayName("UpdatePassword - Account Not Found")
    void updatePassword_AccountNotFound() {
        // Arrange
        int id = 999;
        String oldPassword = "oldPassword123";
        String newPassword = "newPassword123";

        when(accountRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TokenValidException.class,
                () -> accountService.updatePassword(id, oldPassword, newPassword));
        verify(accountRepository, times(1)).findById(id);
        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    @DisplayName("GetProfileById - Success")
    void getProfileById_Success() {
        // Arrange
        int id = 1;
        when(accountRepository.findById(id)).thenReturn(Optional.of(testAccount));

        // Act
        AccountRes result = accountService.getProfileById(id);

        // Assert
        assertNotNull(result);
        assertEquals(testAccount.getId(), result.id());
        assertEquals(testAccount.getEmail(), result.email());
        assertEquals(testAccount.getUsername(), result.username());
        assertEquals(testAccount.getPhone(), result.phone());
        assertEquals(testAccount.getStatus(), result.status());
        assertEquals(testAccount.getRole(), result.role());
        assertEquals(testAccount.getCreatedAt(), result.create_at());
        verify(accountRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("GetProfileById - Account Not Found")
    void getProfileById_AccountNotFound() {
        // Arrange
        int id = 999;
        when(accountRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TokenValidException.class,
                () -> accountService.getProfileById(id));
        verify(accountRepository, times(1)).findById(id);
    }
}
