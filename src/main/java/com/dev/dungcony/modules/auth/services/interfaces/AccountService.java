package com.dev.dungcony.modules.auth.services.interfaces;

import com.dev.dungcony.modules.auth.dtos.res.AccountRes;

public interface AccountService {
    // profile
    AccountRes getProfileById(int id);

    // check
    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    // password
    boolean updatePassword(int id, String oldPassword, String newPassword);

    // email change
    void startChangeEmail(int userId);

    void verifyOldEmailOtp(int userId, String otp);

    void submitNewEmail(int userId, String newEmail);

    void verifyNewEmailOtp(int userId, String otp);

    //
    void verifyEmail(String email);
}
