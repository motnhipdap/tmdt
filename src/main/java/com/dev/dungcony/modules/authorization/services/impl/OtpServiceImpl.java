package com.dev.dungcony.modules.authorization.services.impl;

import com.dev.dungcony.modules.authorization.services.interfaces.OTPService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class OtpServiceImpl implements OTPService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private static final SecureRandom random = new SecureRandom();

    private static final Logger logger = LoggerFactory.getLogger(OtpServiceImpl.class);

    private final int OTP_LENGTH = 6;
    private final String PREFIX = "otp:";
    private final int EXPITRY_SECOND = 30000;

    @Override
    public boolean verifyOTP(String email, String otp) {
        String key = PREFIX + email;
        logger.info("key: {}", key);

        String value = redisTemplate.opsForValue().get(key);
        logger.info("value: {}", value);
        if (value != null && value.equals(otp)) {
            logger.info("otp verify success");
            deleteOTP(email);
            return true;
        }

        return false;
    }

    @Override
    public String createOTP() {

        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(random.nextInt(10));
        }
        return otp.toString();
    }

    @Override
    public void deleteOTP(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public void cacheRedis(String email, String otp) {
        String key = PREFIX + email;
        if (redisTemplate.opsForValue().get(key) != null) {
            deleteOTP(key);
        }
        redisTemplate.opsForValue()
                .set(key, otp, EXPITRY_SECOND, TimeUnit.SECONDS);
    }

    @Override
    public boolean hasValidOtp(String email) {
        String key = PREFIX + email;

        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
}