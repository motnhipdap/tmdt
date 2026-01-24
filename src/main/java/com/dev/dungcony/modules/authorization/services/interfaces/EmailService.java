package com.dev.dungcony.modules.authorization.services.interfaces;

public interface EmailService {
    void SendOtpEmail(String reciever, String OTP);

    void sendWelcomeEmail(String toEmail, String username);

    String buildWelcomeEmailContent(String username);
}
