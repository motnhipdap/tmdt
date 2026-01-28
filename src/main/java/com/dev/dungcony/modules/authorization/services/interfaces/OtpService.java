package com.dev.dungcony.modules.authorization.services.interfaces;

import com.dev.dungcony.modules.authorization.dtos.OtpType;
import com.dev.dungcony.modules.authorization.dtos.requests.VerifyOtpReq;

public interface OtpService {
    void send(String email, OtpType otpType);

    void sendResetPass(String email);

    boolean verifyOTP(VerifyOtpReq req);

}
