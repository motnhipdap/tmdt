package com.dev.dungcony.modules.auth.services.impl;

import com.dev.dungcony.modules.auth.dtos.OtpType;
import com.dev.dungcony.modules.auth.dtos.req.VerifyOtpReq;
import com.dev.dungcony.modules.auth.exceptions.OtpExpireException;
import com.dev.dungcony.modules.auth.helpers.Help;
import com.dev.dungcony.modules.auth.repositories.OtpRegisRepository;
import com.dev.dungcony.modules.auth.services.interfaces.EmailService;
import com.dev.dungcony.modules.auth.services.interfaces.OtpService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OtpServiceImpl implements OtpService {

    private static final Logger logger = LoggerFactory.getLogger(OtpServiceImpl.class);

    private final EmailService emailService;
    private final OtpRegisRepository otpRegisRepo;
    private final PasswordEncoder passwordEncoder;

    private final int OTP_LENGTH = 6;

    @Override
    public void send(String email, OtpType type) {
        if (otpRegisRepo.getValue(key(email, type)) != null)
            otpRegisRepo.delete(key(email, type));

        String otp = Help.createOTP(OTP_LENGTH);
        emailService.send(email, "OTP - dungcony", emailService.buildOtpContent(otp));
        otpRegisRepo.cache(key(email, type), passwordEncoder.encode(otp));
    }

    @Override
    public void sendResetPass(String email) {
        String newPass = Help.createOTP(OTP_LENGTH);
        emailService.send(email, "NEW PASSWORD - dungcony", emailService.buildResetPassContent(newPass));
    }

    @Override
    public boolean verifyOTP(VerifyOtpReq req) {
        String value = otpRegisRepo.getValue(key(req.email(), req.type()));
        logger.info("value: {}", value);

        if (value == null)
            throw new OtpExpireException();
        if (!passwordEncoder.matches(req.otp(), value))
            return false;

        logger.info("otp verify success");
        otpRegisRepo.delete(key(req.email(), req.type()));
        return true;

    }

    private String key(String email, OtpType type) {
        return type.getValue() + ":" + email + ":";
    }

}