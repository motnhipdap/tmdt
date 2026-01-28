package com.dev.dungcony.modules.authorization.services.interfaces;

public interface EmailService {
    void sendOtpEmail(String email, String OTP);

    void sendPasswordReset(String email);

    String buildRandomPassword();
}