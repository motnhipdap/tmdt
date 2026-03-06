package com.dev.dungcony.modules.auth.services.impl;

import com.dev.dungcony.modules.auth.enums.OtpType;
import com.dev.dungcony.modules.auth.dtos.req.VerifyOtpReq;
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
    public void send(String email, OtpType type) {
        if (otpRegisRepo.getValue(key(email, type)) != null)
            otpRegisRepo.delete(key(email, type));

        String otp = generate.otp(OTP_LENGTH);
        emailService.send(email, "OTP - dungcony", emailService.buildOtpContent(otp));
        otpRegisRepo.cache(key(email, type), passwordEncoder.encode(otp));
    }


    @Override
    public boolean verifyOTP(VerifyOtpReq req) {
        String value = otpRegisRepo.getValue(key(req.email(), req.type()));
        log.info("value: {}", value);

        if (value == null)
            throw new OtpExpireException();
        if (!passwordEncoder.matches(req.otp(), value))
            return false;

        log.info("otp verify success");
        otpRegisRepo.delete(key(req.email(), req.type()));
        return true;

    }

    private String key(String email, OtpType type) {
        return type.getValue() + ":" + email + ":";
    }

}