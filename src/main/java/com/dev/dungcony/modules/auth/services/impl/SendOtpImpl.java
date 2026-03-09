package com.dev.dungcony.modules.auth.services.impl;

import com.dev.dungcony.modules.auth.dtos.req.SendOtpChangeEmailReq;
import com.dev.dungcony.modules.auth.enums.OtpType;
import com.dev.dungcony.modules.auth.helpers.Generate;
import com.dev.dungcony.modules.auth.services.interfaces.AccountCheckValidService;
import com.dev.dungcony.modules.auth.services.interfaces.EmailService;
import com.dev.dungcony.modules.auth.services.interfaces.RedisService;
import com.dev.dungcony.modules.auth.services.interfaces.SendOtpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class SendOtpImpl implements SendOtpService {
    private final EmailService emailService;
    private final RedisService redisService;
    private final PasswordEncoder passwordEncoder;
    private final AccountCheckValidService accountCheckValidService;
    private final Generate generate;

    private final int OTP_LENGTH = 6;

    @Override
    public void sendOtpForgotPassword(String email) {
        send(email, OtpType.FORGOT_PASSWORD);
    }

    @Override
    public void sendOtpRegister(String email) {
        send(email, OtpType.REGISTER);
    }

    @Override
    public void sendOtpChangeEmail(int accid, SendOtpChangeEmailReq req) {
        accountCheckValidService.emailAndUsernameIsTrue(accid, req.email(), req.username());

        if (redisService.getValue(generate.key(req.email(), OtpType.CHANGE_EMAIL.getValue())) != null)
            redisService.delete(generate.key(req.email(), OtpType.CHANGE_EMAIL.getValue()));

        String otp = generate.otp(OTP_LENGTH);
        emailService.send(req.email(), "OTP - dungcony", emailService.buildOtpContent(otp));
        redisService.cache(generate.key(req.username(), OtpType.CHANGE_EMAIL.getValue()), passwordEncoder.encode(otp));
    }


    private void send(String email, OtpType type) {
        if (redisService.getValue(generate.key(email, type.getValue())) != null)
            redisService.delete(generate.key(email, type.getValue()));

        String otp = generate.otp(OTP_LENGTH);
        emailService.send(email, "OTP - dungcony", emailService.buildOtpContent(otp));
        redisService.cache(generate.key(email, type.getValue()), passwordEncoder.encode(otp));
    }
}
