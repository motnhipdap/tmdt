package com.dev.dungcony.modules.authorization.services.interfaces;

public interface OTPService {

    boolean verifyOTP(String email, String otp);

    String createOTP();

    void deleteOTP(String key);

    void cacheRedis(String email, String otp);

    boolean hasValidOtp(String email);

}
