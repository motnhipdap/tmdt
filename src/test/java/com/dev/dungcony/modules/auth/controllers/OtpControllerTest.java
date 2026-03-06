package com.dev.dungcony.modules.auth.controllers;

import com.dev.dungcony.modules.auth.TestConfig;
import com.dev.dungcony.modules.auth.enums.OtpType;
import com.dev.dungcony.modules.auth.dtos.req.SendOtpReq;
import com.dev.dungcony.modules.auth.dtos.req.VerifyOtpReq;
import com.dev.dungcony.modules.auth.exceptions.OtpExpireException;
import com.dev.dungcony.modules.auth.services.interfaces.OtpService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = OtpController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(TestConfig.class)
@DisplayName("OtpController Unit Tests")
class OtpControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private OtpService otpService;

    @MockitoBean
    private com.dev.dungcony.modules.auth.services.interfaces.JwtService jwtService;

    @MockitoBean
    private com.dev.dungcony.modules.auth.services.interfaces.AccountService accountService;

    @Test
    @DisplayName("Send Registration OTP - Success")
    void sendRegisOtp_Success() throws Exception {
        // Arrange
        SendOtpReq req = new SendOtpReq("test@example.com", OtpType.REGISTER);
        doNothing().when(otpService).send(anyString(), any(OtpType.class));

        // Act & Assert
        mockMvc.perform(post("/v1/api/auth/send-regis-otp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("send otp successfully"));

        verify(otpService, times(1)).send(req.email(), req.otpType());
    }

    @Test
    @DisplayName("Send Reset Password OTP - Success")
    void sendResetPasswordOtp_Success() throws Exception {
        // Arrange
        SendOtpReq req = new SendOtpReq("test@example.com", OtpType.FORGOT_PASSWORD);
        doNothing().when(otpService).send(anyString(), any(OtpType.class));

        // Act & Assert
        mockMvc.perform(post("/v1/api/auth/send-reset-password-otp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("send otp successfully"));

        verify(otpService, times(1)).send(req.email(), req.otpType());
    }

    @Test
    @DisplayName("Verify OTP - Success")
    void verifyOtp_Success() throws Exception {
        // Arrange
        VerifyOtpReq req = new VerifyOtpReq("test@example.com", "123456", OtpType.REGISTER);
        when(otpService.verifyOTP(any(VerifyOtpReq.class))).thenReturn(true);

        // Act & Assert
        mockMvc.perform(post("/v1/api/auth/verify-otp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("otp verify res"))
                .andExpect(jsonPath("$.data").value(true));

        verify(otpService, times(1)).verifyOTP(any(VerifyOtpReq.class));
    }

    @Test
    @DisplayName("Verify OTP - Wrong OTP")
    void verifyOtp_WrongOtp() throws Exception {
        // Arrange
        VerifyOtpReq req = new VerifyOtpReq("test@example.com", "654321", OtpType.REGISTER);
        when(otpService.verifyOTP(any(VerifyOtpReq.class))).thenReturn(false);

        // Act & Assert
        mockMvc.perform(post("/v1/api/auth/verify-otp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value(false));

        verify(otpService, times(1)).verifyOTP(any(VerifyOtpReq.class));
    }

    @Test
    @DisplayName("Verify OTP - Expired OTP")
    void verifyOtp_ExpiredOtp() throws Exception {
        // Arrange
        VerifyOtpReq req = new VerifyOtpReq("test@example.com", "123456", OtpType.REGISTER);
        when(otpService.verifyOTP(any(VerifyOtpReq.class))).thenThrow(new OtpExpireException());

        // Act & Assert
        mockMvc.perform(post("/v1/api/auth/verify-otp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isGone());

        verify(otpService, times(1)).verifyOTP(any(VerifyOtpReq.class));
    }

    @Test
    @DisplayName("Send Registration OTP - Validation Error (Invalid Email)")
    void sendRegisOtp_ValidationError_InvalidEmail() throws Exception {
        // Arrange
        SendOtpReq req = new SendOtpReq("invalid-email", OtpType.REGISTER);

        // Act & Assert
        mockMvc.perform(post("/v1/api/auth/send-regis-otp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());

        verify(otpService, never()).send(anyString(), any(OtpType.class));
    }
}
