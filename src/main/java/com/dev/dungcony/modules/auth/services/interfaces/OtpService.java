package com.dev.dungcony.modules.auth.services.interfaces;

import com.dev.dungcony.modules.auth.dtos.req.VerifyOtpForgotPasswordReq;
import com.dev.dungcony.modules.auth.enums.OtpType;
import com.dev.dungcony.modules.auth.dtos.req.VerifyOtpRegisterReq;

public interface OtpService {
    void sendOtpForgotPassword(String email);

    void sendOtpRegister(String email);

    boolean verifyOtpRegister(VerifyOtpRegisterReq req);

    boolean verifyOtpForgotPassword(VerifyOtpForgotPasswordReq req);
}
