package com.dev.dungcony.modules.authorization.services.impl;

import com.dev.dungcony.modules.authorization.dtos.requests.VerifyOtpReq;
import com.dev.dungcony.modules.authorization.helpers.Help;
import com.dev.dungcony.modules.authorization.repositories.OtpRegisRepository;
import com.dev.dungcony.modules.authorization.services.interfaces.EmailService;
import com.dev.dungcony.modules.authorization.services.interfaces.OtpRegisService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OtpRegiserviceImpl implements OtpRegisService {

    private static final Logger logger = LoggerFactory.getLogger(OtpRegiserviceImpl.class);

    private final EmailService emailService;
    private final OtpRegisRepository otpRegisRepo;

    private final int OTP_LENGTH = 6;

    @Override
    public void sendOtp(String email) {
        if (otpRegisRepo.getValue(key(email)) != null)
            otpRegisRepo.delete(key(email));

        String otp = Help.createOTP(OTP_LENGTH);

        emailService.sendOtpEmail(email, otp);

        otpRegisRepo.cache(key(email), otp);
    }

    @Override
    public boolean verifyOTP(VerifyOtpReq req) {
        String value = redis.get(key);
        logger.info("value: {}", value);
        if (value != null && value.equals(req.otp())) {
            logger.info("otp verify success");
            redis.delete(key);
            return true;
        }
        return false;
    }

    private String key(String email) {
        return "otp:regis:" + email + ":";
    }

}