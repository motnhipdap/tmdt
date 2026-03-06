package com.dev.dungcony.modules.auth.services.interfaces;

import com.dev.dungcony.modules.auth.enums.OtpType;
import com.dev.dungcony.modules.auth.dtos.req.VerifyOtpReq;

public interface OtpService {
    void send(String email, OtpType otpType);


    boolean verifyOTP(VerifyOtpReq req);
}
