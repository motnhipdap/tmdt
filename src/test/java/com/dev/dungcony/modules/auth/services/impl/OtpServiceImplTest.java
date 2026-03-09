package com.dev.dungcony.modules.auth.services.impl;

import com.dev.dungcony.modules.auth.enums.OtpType;
import com.dev.dungcony.modules.auth.exceptions.OtpExpireException;
import com.dev.dungcony.modules.auth.repositories.OtpRegisRepository;
import com.dev.dungcony.modules.auth.services.interfaces.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("OtpService Unit Tests")
class OtpServiceImplTest {

    @Mock
    private EmailService emailService;

    @Mock
    private OtpRegisRepository otpRegisRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private OtpServiceImpl otpService;

    private String testEmail;
    private OtpType testOtpType;

    @BeforeEach
    void setUp() {
        testEmail = "test@example.com";
        testOtpType = OtpType.REGISTER;
    }

    @Test
    @DisplayName("Send OTP - Success with Existing OTP")
    void send_SuccessWithExistingOtp() {
        // Arrange
        String key = testOtpType.getValue() + ":" + testEmail + ":";
        when(otpRegisRepository.getValue(key)).thenReturn("existingOtp");
        when(passwordEncoder.encode(anyString())).thenReturn("encodedOtp");
        when(emailService.buildOtpContent(anyString())).thenReturn("OTP Content");
        doNothing().when(emailService).send(anyString(), anyString(), anyString());
        doNothing().when(otpRegisRepository).delete(key);
        doNothing().when(otpRegisRepository).cache(anyString(), anyString());

        // Act
        assertDoesNotThrow(() -> otpService.send(testEmail, testOtpType));

        // Assert
        verify(otpRegisRepository, times(1)).getValue(key);
        verify(otpRegisRepository, times(1)).delete(key);
        verify(emailService, times(1)).buildOtpContent(anyString());
        verify(emailService, times(1)).send(eq(testEmail), eq("OTP - dungcony"), anyString());
        verify(passwordEncoder, times(1)).encode(anyString());
        verify(otpRegisRepository, times(1)).cache(eq(key), anyString());
    }

    @Test
    @DisplayName("Send OTP - Success without Existing OTP")
    void send_SuccessWithoutExistingOtp() {
        // Arrange
        String key = testOtpType.getValue() + ":" + testEmail + ":";
        when(otpRegisRepository.getValue(key)).thenReturn(null);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedOtp");
        when(emailService.buildOtpContent(anyString())).thenReturn("OTP Content");
        doNothing().when(emailService).send(anyString(), anyString(), anyString());
        doNothing().when(otpRegisRepository).cache(anyString(), anyString());

        // Act
        assertDoesNotThrow(() -> otpService.send(testEmail, testOtpType));

        // Assert
        verify(otpRegisRepository, times(1)).getValue(key);
        verify(otpRegisRepository, never()).delete(key);
        verify(emailService, times(1)).buildOtpContent(anyString());
        verify(emailService, times(1)).send(eq(testEmail), eq("OTP - dungcony"), anyString());
        verify(passwordEncoder, times(1)).encode(anyString());
        verify(otpRegisRepository, times(1)).cache(eq(key), anyString());
    }

    @Test
    @DisplayName("Send Reset Password - Success")
    void sendResetPass_Success() {
        // Arrange
        when(emailService.buildResetPassContent(anyString())).thenReturn("Reset Password Content");
        doNothing().when(emailService).send(anyString(), anyString(), anyString());

        // Act
        assertDoesNotThrow(() -> otpService.sendResetPass(testEmail));

        // Assert
        verify(emailService, times(1)).buildResetPassContent(anyString());
        verify(emailService, times(1)).send(eq(testEmail), eq("NEW PASSWORD - dungcony"), anyString());
    }

    @Test
    @DisplayName("Verify OTP - Success")
    void verifyOTP_Success() {
        // Arrange
        String otp = "123456";
        String encodedOtp = "encodedOtp";
        String key = testOtpType.getValue() + ":" + testEmail + ":";
        VerifyOtpRegisterReq req = new VerifyOtpRegisterReq(testEmail, otp, testOtpType);

        when(otpRegisRepository.getValue(key)).thenReturn(encodedOtp);
        when(passwordEncoder.matches(otp, encodedOtp)).thenReturn(true);
        doNothing().when(otpRegisRepository).delete(key);

        // Act
        boolean result = otpService.verifyOTP(req);

        // Assert
        assertTrue(result);
        verify(otpRegisRepository, times(1)).getValue(key);
        verify(passwordEncoder, times(1)).matches(otp, encodedOtp);
        verify(otpRegisRepository, times(1)).delete(key);
    }

    @Test
    @DisplayName("Verify OTP - Wrong OTP")
    void verifyOTP_WrongOtp() {
        // Arrange
        String otp = "123456";
        String wrongOtp = "654321";
        String encodedOtp = "encodedOtp";
        String key = testOtpType.getValue() + ":" + testEmail + ":";
        VerifyOtpRegisterReq req = new VerifyOtpRegisterReq(testEmail, wrongOtp, testOtpType);

        when(otpRegisRepository.getValue(key)).thenReturn(encodedOtp);
        when(passwordEncoder.matches(wrongOtp, encodedOtp)).thenReturn(false);

        // Act
        boolean result = otpService.verifyOTP(req);

        // Assert
        assertFalse(result);
        verify(otpRegisRepository, times(1)).getValue(key);
        verify(passwordEncoder, times(1)).matches(wrongOtp, encodedOtp);
        verify(otpRegisRepository, never()).delete(key);
    }

    @Test
    @DisplayName("Verify OTP - Expired OTP")
    void verifyOTP_ExpiredOtp() {
        // Arrange
        String otp = "123456";
        String key = testOtpType.getValue() + ":" + testEmail + ":";
        VerifyOtpRegisterReq req = new VerifyOtpRegisterReq(testEmail, otp, testOtpType);

        when(otpRegisRepository.getValue(key)).thenReturn(null);

        // Act & Assert
        assertThrows(OtpExpireException.class, () -> otpService.verifyOTP(req));
        verify(otpRegisRepository, times(1)).getValue(key);
        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(otpRegisRepository, never()).delete(key);
    }
}
