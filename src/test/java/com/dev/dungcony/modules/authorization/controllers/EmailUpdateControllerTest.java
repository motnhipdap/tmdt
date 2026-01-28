package com.dev.dungcony.modules.authorization.controllers;

import com.dev.dungcony.modules.authorization.TestConfig;
import com.dev.dungcony.modules.authorization.helpers.AccountDetails;
import com.dev.dungcony.modules.authorization.services.interfaces.EmailChangeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

@WebMvcTest(controllers = EmailUpdateController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(TestConfig.class)
@DisplayName("EmailUpdateController Unit Tests")
class EmailUpdateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private EmailChangeService emailChangeService;

    @MockitoBean
    private com.dev.dungcony.modules.authorization.services.interfaces.JwtService jwtService;

    @MockitoBean
    private com.dev.dungcony.modules.authorization.services.interfaces.AccountService accountService;

    private AccountDetails accountDetails;
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        accountDetails = new AccountDetails(1, "testuser", "customer");
        authentication = new UsernamePasswordAuthenticationToken(
                accountDetails, null, accountDetails.getAuthorities());
    }

    @Test
    @DisplayName("Update Request - Send OTP to Old Email - Success")
    @WithMockUser(username = "testuser")
    void updateReq_SendOtpToOldEmail_Success() throws Exception {
        // Arrange
        doNothing().when(emailChangeService).startChangeEmail(anyInt());

        // Act & Assert
        mockMvc.perform(post("/v1/api/email/update/send-old-email")
                .with(authentication(authentication)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("send otp req update email successfully"));

        verify(emailChangeService, times(1)).startChangeEmail(1);
    }

    @Test
    @DisplayName("Verify Old Email - Success")
    @WithMockUser(username = "testuser")
    void verifyOldEmail_Success() throws Exception {
        // Arrange
        String otp = "123456";
        doNothing().when(emailChangeService).verifyOldEmailOtp(anyInt(), anyString());

        // Act & Assert
        mockMvc.perform(post("/v1/api/email/update/verify-old-email")
                .with(authentication(authentication))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(otp)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("successfully"));

        verify(emailChangeService, times(1)).verifyOldEmailOtp(1, "\"" + otp + "\"");
    }

    @Test
    @DisplayName("Send New Email - Success")
    @WithMockUser(username = "testuser")
    void sendNewEmail_Success() throws Exception {
        // Arrange
        String newEmail = "newemail@example.com";
        doNothing().when(emailChangeService).submitNewEmail(anyInt(), anyString());

        // Act & Assert
        mockMvc.perform(post("/v1/api/email/update/send-new-email")
                .with(authentication(authentication))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newEmail)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("send to email successfully"));

        verify(emailChangeService, times(1)).submitNewEmail(eq(1), anyString());
    }

    @Test
    @DisplayName("Success - Verify New Email OTP - Success")
    @WithMockUser(username = "testuser")
    void success_VerifyNewEmailOtp_Success() throws Exception {
        // Arrange
        String otp = "123456";
        doNothing().when(emailChangeService).verifyNewEmailOtp(anyInt(), anyString());

        // Act & Assert
        mockMvc.perform(put("/v1/api/email/update/success")
                .with(authentication(authentication))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(otp)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("email update successfully"));

        verify(emailChangeService, times(1)).verifyNewEmailOtp(eq(1), anyString());
    }
}
