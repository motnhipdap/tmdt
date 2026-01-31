package com.dev.dungcony.modules.auth.services.interfaces;

public interface EmailService {
    void send(String email, String subject, String body);

    String buildOtpContent(String otp);

    String buildResetPassContent(String newPas);
}