package com.dev.dungcony.modules.authorization.services.interfaces;

public interface EmailChangeService {
    void startChangeEmail(int userId);

    void verifyOldEmailOtp(int userId, String otp);

    void submitNewEmail(int userId, String newEmail);

    void verifyNewEmailOtp(int userId, String otp);
}
