package com.dev.dungcony.modules.auth.services.interfaces;

import com.dev.dungcony.modules.auth.dtos.req.VerifyOtpReq;

public interface OtpService {
    void sendOtpForgotPassword(String email);

    void sendOtpRegister(String email);

    void sendOtpChangeEmail(String email);

    boolean verifyOtpRegister(VerifyOtpReq req);

    boolean verifyOtpForgotPassword(VerifyOtpReq req);

    boolean verifyOtpEmailChange(VerifyOtpReq req);

}
