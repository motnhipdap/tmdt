package com.dev.dungcony.modules.auth.services.impl;

import com.dev.dungcony.modules.auth.dtos.req.VerifyOtpEmailChangeReq;
import com.dev.dungcony.modules.auth.dtos.req.VerifyOtpReq;
import com.dev.dungcony.modules.auth.enums.OtpType;
import com.dev.dungcony.modules.auth.exceptions.OtpExpireException;
import com.dev.dungcony.modules.auth.helpers.Generate;
import com.dev.dungcony.modules.auth.services.interfaces.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class VerifyOtpImpl implements VeriyOtpService {
    private final EmailService emailService;
    private final RedisService redisService;
    private final PasswordEncoder passwordEncoder;
    private final AccountCheckValidService accountCheckValidService;
    private final AccountUpdateService accountUpdateService;
    private final Generate generate;


    @Override
    public boolean verifyOtpRegister(VerifyOtpReq req) {
        String value = redisService.getValue(generate.key(req.email(), OtpType.REGISTER.getValue()));
        log.info("value regis: {}", value);

        if (value == null)
            throw new OtpExpireException();
        if (!passwordEncoder.matches(req.otp(), value))
            return false;

        log.info("otpregis verify success");
        redisService.delete(generate.key(req.email(), OtpType.REGISTER.getValue()));
        return true;
    }

    @Override
    public boolean verifyOtpForgotPassword(VerifyOtpReq req) {
        String value = redisService.getValue(generate.key(req.email(), OtpType.FORGOT_PASSWORD.getValue()));
        log.info("value forgot: {}", value);

        if (value == null)
            throw new OtpExpireException();
        if (!passwordEncoder.matches(req.otp(), value))
            return false;

        log.info("otpforgot verify success");
        redisService.delete(generate.key(req.email(), OtpType.FORGOT_PASSWORD.getValue()));
        return true;
    }

    @Override
    public boolean verifyOtpEmailChange(int accid, VerifyOtpEmailChangeReq req) {
        String value = redisService.getValue(generate.key(req.username(), OtpType.CHANGE_EMAIL.getValue()));
        log.info("value emailchange: {}", value);

        if (value == null)
            throw new OtpExpireException();
        if (!passwordEncoder.matches(req.otp(), value))
            return false;

        log.info("otpemailchange verify success");

        accountUpdateService.updateEmail(accid, req.newEmail());
        redisService.delete(generate.key(req.username(), OtpType.CHANGE_EMAIL.getValue()));
        return true;
    }

}
