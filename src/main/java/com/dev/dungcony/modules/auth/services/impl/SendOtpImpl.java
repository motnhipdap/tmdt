package com.dev.dungcony.modules.auth.services.impl;

import com.dev.dungcony.modules.auth.dtos.req.SendOtpChangeEmailReq;
import com.dev.dungcony.modules.auth.enums.OtpType;
import com.dev.dungcony.modules.auth.helpers.Generate;
import com.dev.dungcony.modules.auth.services.interfaces.AccountCheckService;
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
    private final AccountCheckService accountCheckService;
    private final Generate generate;

    private final int OTP_LENGTH = 6;

    @Override
    public void sendOtpRegister(String email) {
        if (redisService.getValue(generate.key(email, OtpType.REGISTER.getValue())) != null)
            redisService.delete(generate.key(email, OtpType.REGISTER.getValue()));

        String otp = generate.otp(OTP_LENGTH);
        emailService.sendOtpRegis(email, otp);
        redisService.cache(generate.key(email, OtpType.REGISTER.getValue()), passwordEncoder.encode(otp));
    }

    @Override
    public void sendOtpChangeEmail(int accid, SendOtpChangeEmailReq req) {
        accountCheckService.emailAndUsernameIsTrue(accid, req.email(), req.username());

        if (redisService.getValue(generate.key(req.email(), OtpType.CHANGE_EMAIL.getValue())) != null)
            redisService.delete(generate.key(req.email(), OtpType.CHANGE_EMAIL.getValue()));

        String otp = generate.otp(OTP_LENGTH);
        emailService.sendOtpChangeEmail(req.email(), otp);
        redisService.cache(generate.key(req.username(), OtpType.CHANGE_EMAIL.getValue()), passwordEncoder.encode(otp));
    }
}
