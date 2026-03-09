package com.dev.dungcony.modules.auth.controllers;

import com.dev.dungcony.commons.dtos.AccountDetails;
import com.dev.dungcony.modules.auth.TestConfig;
import com.dev.dungcony.modules.auth.controllers.store.AccountController;
import com.dev.dungcony.modules.auth.dtos.req.UpdatePasswordReq;
import com.dev.dungcony.modules.auth.dtos.res.AccountRes;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AccountController.class)
@AutoConfigureMockMvc(addFilters = true)
@Import(TestConfig.class)
@DisplayName("AccountController Unit Tests")
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AccountService accountService;

    @MockitoBean
    private com.dev.dungcony.modules.auth.services.interfaces.JwtService jwtService;

    private AccountRes accountRes;
    private AccountDetails accountDetails;
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        accountRes = new AccountRes(
                1,
                "test@example.com",
                "testuser",
                "0123456789",
                "active",
                "customer",
                LocalDateTime.now());

        accountDetails = new AccountDetails(1, "testuser", "customer");
        authentication = new UsernamePasswordAuthenticationToken(
                accountDetails, null, accountDetails.getAuthorities());
    }

    @Test
    @DisplayName("Get Me - Success")
    @WithMockUser(username = "testuser")
    void getMe_Success() throws Exception {
        // Arrange
        when(accountService.getProfileById(anyInt())).thenReturn(accountRes);

        // Act & Assert
        mockMvc.perform(get("/v1/api/account/me")
                        .with(authentication(authentication)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.email").value("test@example.com"))
                .andExpect(jsonPath("$.data.username").value("testuser"))
                .andExpect(jsonPath("$.data.phone").value("0123456789"))
                .andExpect(jsonPath("$.data.status").value("active"))
                .andExpect(jsonPath("$.data.role").value("customer"));

        verify(accountService, times(1)).getProfileById(1);
    }

    @Test
    @DisplayName("Check Email - Email Exists")
    void checkEmail_EmailExists() throws Exception {
        // Arrange
        String email = "test@example.com";
        when(accountService.existsByEmail(email)).thenReturn(true);

        // Act & Assert
        mockMvc.perform(get("/v1/api/account/check_email")
                        .param("email", email))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Email already exists!"))
                .andExpect(jsonPath("$.data").value(true));

        verify(accountService, times(1)).existsByEmail(email);
    }

    @Test
    @DisplayName("Check Email - Email Does Not Exist")
    void checkEmail_EmailDoesNotExist() throws Exception {
        // Arrange
        String email = "new@example.com";
        when(accountService.existsByEmail(email)).thenReturn(false);

        // Act & Assert
        mockMvc.perform(get("/v1/api/account/check_email")
                        .param("email", email))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.data").value(false));

        verify(accountService, times(1)).existsByEmail(email);
    }

    @Test
    @DisplayName("Check Username - Username Exists")
    void checkUsername_UsernameExists() throws Exception {
        // Arrange
        String username = "testuser";
        when(accountService.existsByUsername(username)).thenReturn(true);

        // Act & Assert
        mockMvc.perform(get("/v1/api/account/check_username")
                        .param("username", username))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("username already exists!"))
                .andExpect(jsonPath("$.data").value(true));

        verify(accountService, times(1)).existsByUsername(username);
    }

    @Test
    @DisplayName("Check Username - Username Does Not Exist")
    void checkUsername_UsernameDoesNotExist() throws Exception {
        // Arrange
        String username = "newuser";
        when(accountService.existsByUsername(username)).thenReturn(false);

        // Act & Assert
        mockMvc.perform(get("/v1/api/account/check_username")
                        .param("username", username))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.data").value(false));

        verify(accountService, times(1)).existsByUsername(username);
    }

    @Test
    @DisplayName("Update Password - Success")
        // @WithMockUser(username = "testuser")
    void updatePassword_Success() throws Exception {
        // Arrange
        UpdatePasswordReq req = new UpdatePasswordReq("oldPassword123", "newPassword123");

        AccountDetails accountDetails = new AccountDetails(1, "testuser", "customer");

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                accountDetails,
                null,
                accountDetails.getAuthorities());

        when(accountService.updatePassword(anyInt(), anyString(), anyString())).thenReturn(true);

        // Act & Assert
        mockMvc.perform(put("/v1/api/account/update_password")
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("update_password res"))
                .andExpect(jsonPath("$.data").value(true));

        verify(accountService, times(1)).updatePassword(1, "oldPassword123", "newPassword123");
    }

    @Test
    @DisplayName("Update Password - Wrong Old Password")
    @WithMockUser(username = "testuser")
    void updatePassword_WrongOldPassword() throws Exception {
        // Arrange
        UpdatePasswordReq req = new UpdatePasswordReq("wrongOldPassword", "newPassword123");
        when(accountService.updatePassword(anyInt(), anyString(), anyString())).thenReturn(false);

        // Act & Assert
        mockMvc.perform(put("/v1/api/account/update_password")
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value(false));

        verify(accountService, times(1)).updatePassword(1, "wrongOldPassword", "newPassword123");
    }

    @Test
    @DisplayName("Update Password - Validation Error (Empty Password)")
    @WithMockUser(username = "testuser")
    void updatePassword_ValidationError_EmptyPassword() throws Exception {
        // Arrange
        UpdatePasswordReq req = new UpdatePasswordReq("", "newPassword123");

        // Act & Assert
        mockMvc.perform(put("/v1/api/account/update_password")
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());

        verify(accountService, never()).updatePassword(anyInt(), anyString(), anyString());
    }
}
