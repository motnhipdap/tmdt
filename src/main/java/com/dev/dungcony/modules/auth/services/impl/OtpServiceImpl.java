package com.dev.dungcony.modules.auth.services.impl;

import com.dev.dungcony.modules.auth.dtos.req.VerifyOtpReq;
import com.dev.dungcony.modules.auth.enums.OtpType;
import com.dev.dungcony.modules.auth.exceptions.OtpExpireException;
import com.dev.dungcony.modules.auth.helpers.Generate;
import com.dev.dungcony.modules.auth.repositories.OtpRegisRepository;
import com.dev.dungcony.modules.auth.services.interfaces.EmailService;
import com.dev.dungcony.modules.auth.services.interfaces.OtpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class OtpServiceImpl implements OtpService {

    private final EmailService emailService;
    private final OtpRegisRepository otpRegisRepo;
    private final PasswordEncoder passwordEncoder;
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
    public void sendOtpChangeEmail(String email) {
        send(email, OtpType.CHANGE_EMAIL);
    }

    @Override
    public boolean verifyOtpRegister(VerifyOtpReq req) {
        String value = otpRegisRepo.getValue(key(req.email(), OtpType.REGISTER));
        log.info("value regis: {}", value);

        if (value == null)
            throw new OtpExpireException();
        if (!passwordEncoder.matches(req.otp(), value))
            return false;

        log.info("otpregis verify success");
        otpRegisRepo.delete(key(req.email(), OtpType.REGISTER));
        return true;
    }

    @Override
    public boolean verifyOtpForgotPassword(VerifyOtpReq req) {
        String value = otpRegisRepo.getValue(key(req.email(), OtpType.FORGOT_PASSWORD));
        log.info("value forgot: {}", value);

        if (value == null)
            throw new OtpExpireException();
        if (!passwordEncoder.matches(req.otp(), value))
            return false;

        log.info("otpforgot verify success");
        otpRegisRepo.delete(key(req.email(), OtpType.FORGOT_PASSWORD));
        return true;
    }

    @Override
    public boolean verifyOtpEmailChange(VerifyOtpReq req) {
        String value = otpRegisRepo.getValue(key(req.email(), OtpType.CHANGE_EMAIL));
        log.info("value emailchange: {}", value);

        if (value == null)
            throw new OtpExpireException();
        if (!passwordEncoder.matches(req.otp(), value))
            return false;

        log.info("otpemailchange verify success");
        otpRegisRepo.delete(key(req.email(), OtpType.CHANGE_EMAIL));
        return true;
    }


    // ========================= Private Methods =========================

    private String key(String email, OtpType type) {
        return type.getValue() + ":" + email + ":";
    }

    private void send(String email, OtpType type) {
        if (otpRegisRepo.getValue(key(email, type)) != null)
            otpRegisRepo.delete(key(email, type));

        String otp = generate.otp(OTP_LENGTH);
        emailService.send(email, "OTP - dungcony", emailService.buildOtpContent(otp));
        otpRegisRepo.cache(key(email, type), passwordEncoder.encode(otp));
    }


}