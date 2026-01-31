package com.dev.dungcony.modules.auth.services.interfaces;

import com.dev.dungcony.modules.auth.dtos.OtpType;
import com.dev.dungcony.modules.auth.dtos.requests.VerifyOtpReq;

public interface OtpService {
    void send(String email, OtpType otpType);

    void sendResetPass(String email);

    boolean verifyOTP(VerifyOtpReq req);

}
