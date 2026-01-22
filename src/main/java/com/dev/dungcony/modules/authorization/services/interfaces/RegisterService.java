package com.dev.dungcony.modules.authorization.services.interfaces;

import com.dev.dungcony.modules.authorization.dtos.VerifyOtpRequest;
import com.dev.dungcony.modules.authorization.entities.Account;

public interface RegisterService {

    /**
     * Gửi OTP về email
     */
    void sendOtp(String email);

    /**
     * Xác thực OTP và tạo tài khoản
     */
    Account verifyOtpAndRegister(VerifyOtpRequest request);
}
