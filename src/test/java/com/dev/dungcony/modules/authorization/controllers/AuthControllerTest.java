package com.dev.dungcony.modules.authorization.controllers;

import com.dev.dungcony.modules.authorization.TestConfig;
import com.dev.dungcony.modules.authorization.dtos.requests.LoginReq;
import com.dev.dungcony.modules.authorization.dtos.requests.RegisReq;
import com.dev.dungcony.modules.authorization.dtos.responses.LoginRes;
import com.dev.dungcony.modules.authorization.dtos.responses.LoginResult;
import com.dev.dungcony.modules.authorization.exceptions.EmailExistException;
import com.dev.dungcony.modules.authorization.exceptions.InvalidCredentialsException;
import com.dev.dungcony.modules.authorization.services.interfaces.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(TestConfig.class)
@DisplayName("AuthController Unit Tests")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private com.dev.dungcony.modules.authorization.services.interfaces.JwtService jwtService;

    @MockitoBean
    private com.dev.dungcony.modules.authorization.services.interfaces.AccountService accountService;

    private LoginReq loginReq;
    private RegisReq regisReq;
    private LoginResult loginResult;

    @BeforeEach
    void setUp() {
        loginReq = new LoginReq("testuser", "password123");
        regisReq = new RegisReq("test@example.com", "testuser", "password123");
        loginResult = new LoginResult(
                "jwt.token.here",
                "Bearer",
                3600000L,
                "refresh_token=refresh.token.here; HttpOnly; Secure; Path=/v1/api/auth/refresh; Max-Age=7200");
    }

    @Test
    @DisplayName("Login - Success")
    void login_Success() throws Exception {
        // Arrange
        when(authService.login(anyString(), anyString())).thenReturn(loginResult);

        // Act & Assert
        mockMvc.perform(post("/v1/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginReq)))
                .andExpect(status().isOk())
                .andExpect(header().exists("Set-Cookie"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("login success"))
                .andExpect(jsonPath("$.data.token").value("jwt.token.here"))
                .andExpect(jsonPath("$.data.expiration").value(3600000));

        verify(authService, times(1)).login(loginReq.username(), loginReq.password());
    }

    @Test
    @DisplayName("Login - Invalid Credentials")
    void login_InvalidCredentials() throws Exception {
        // Arrange
        when(authService.login(anyString(), anyString())).thenThrow(new InvalidCredentialsException());

        // Act & Assert
        mockMvc.perform(post("/v1/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginReq)))
                .andExpect(status().isUnauthorized());

        verify(authService, times(1)).login(loginReq.username(), loginReq.password());
    }

    @Test
    @DisplayName("Login - Validation Error (Empty Username)")
    void login_ValidationError_EmptyUsername() throws Exception {
        // Arrange
        LoginReq invalidReq = new LoginReq("", "password123");

        // Act & Assert
        mockMvc.perform(post("/v1/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidReq)))
                .andExpect(status().isBadRequest());

        verify(authService, never()).login(anyString(), anyString());
    }

    @Test
    @DisplayName("Register - Success")
    void register_Success() throws Exception {
        // Arrange
        doNothing().when(authService).register(any(RegisReq.class));

        // Act & Assert
        mockMvc.perform(post("/v1/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(regisReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("register success"));

        verify(authService, times(1)).register(any(RegisReq.class));
    }

    @Test
    @DisplayName("Register - Email Already Exists")
    void register_EmailAlreadyExists() throws Exception {
        // Arrange
        doThrow(new EmailExistException()).when(authService).register(any(RegisReq.class));

        // Act & Assert
        mockMvc.perform(post("/v1/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(regisReq)))
                .andExpect(status().isConflict());

        verify(authService, times(1)).register(any(RegisReq.class));
    }

    @Test
    @DisplayName("Register - Validation Error (Invalid Email)")
    void register_ValidationError_InvalidEmail() throws Exception {
        // Arrange
        RegisReq invalidReq = new RegisReq("invalid-email", "testuser", "password123");

        // Act & Assert
        mockMvc.perform(post("/v1/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidReq)))
                .andExpect(status().isBadRequest());

        verify(authService, never()).register(any(RegisReq.class));
    }

    @Test
    @DisplayName("Refresh Token - Success")
    void refreshToken_Success() throws Exception {
        // Arrange
        LoginRes loginRes = new LoginRes("new.jwt.token.here", 3600000L);
        when(authService.refreshToken(anyString())).thenReturn(loginRes);

        // Act & Assert
        mockMvc.perform(post("/v1/api/auth/refresh")
                .cookie(new jakarta.servlet.http.Cookie("refresh_token", "refresh.token.here")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("refresh_success"))
                .andExpect(jsonPath("$.data.token").value("new.jwt.token.here"));

        verify(authService, times(1)).refreshToken("refresh.token.here");
    }

    @Test
    @DisplayName("Logout - Success")
    void logout_Success() throws Exception {
        // Arrange
        doNothing().when(authService).logout(anyString());

        // Act & Assert
        mockMvc.perform(post("/v1/api/auth/logout")
                .cookie(new jakarta.servlet.http.Cookie("refresh_token", "refresh.token.here")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("logout success"));

        verify(authService, times(1)).logout("refresh.token.here");
    }
}
