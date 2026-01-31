package com.dev.dungcony.modules.auth.services.impl;

import com.dev.dungcony.modules.auth.config.Env;
import com.dev.dungcony.modules.auth.entities.Account;
import com.dev.dungcony.modules.auth.exceptions.TokenExpireException;
import com.dev.dungcony.modules.auth.exceptions.TokenValidException;
import com.dev.dungcony.modules.auth.helpers.Help;
import com.dev.dungcony.modules.auth.repositories.AccountRepository;
import com.dev.dungcony.modules.auth.repositories.EmailChangeRedisRepository;
import com.dev.dungcony.modules.auth.services.interfaces.EmailChangeService;
import com.dev.dungcony.modules.auth.services.interfaces.EmailService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class EmailChangeServiceImpl implements EmailChangeService {

    private static final Logger logger = LoggerFactory.getLogger(EmailChangeServiceImpl.class);

    private final int OTP_LENGTH = 6;
    private final AccountRepository accountRepo;
    private final EmailChangeRedisRepository redisRepo;
    private final EmailService emailService;
    private final PasswordEncoder encoder;

    @Override
    public void startChangeEmail(int userId) {

        if (redisRepo.exists(userId))
            throw new IllegalStateException("Email change in progress");

        Account acc = accountRepo.findById(userId).orElseThrow(TokenValidException::new);

        String otp = Help.createOTP(OTP_LENGTH);
        String hash = encoder.encode(otp);

        redisRepo.createRequest(
                userId,
                acc.getEmail(),
                hash,
                Env.expiration_change_email);

        emailService.send(acc.getEmail(), "OTP yêu cầu thay đổi email - dungcony", otp);
        logger.info("Email change request sent to: {}", acc.getEmail());
    }

    @Override
    public void verifyOldEmailOtp(int userId, String otp) {

        String step = redisRepo.getField(userId, "step");
        if (!"VERIFY_OLD_EMAIL".equals(step))
            throw new IllegalStateException("Invalid step");

        String hash = redisRepo.getField(userId, "old_otp_hash");

        if (!encoder.matches(otp, hash))
            throw new IllegalArgumentException("OTP invalid");

        redisRepo.setField(userId, "old_verified", "true");
        redisRepo.setField(userId, "step", "INPUT_NEW_EMAIL");
    }

    @Override
    public void submitNewEmail(int userId, String newEmail) {

        String step = redisRepo.getField(userId, "step");
        if (!"INPUT_NEW_EMAIL".equals(step))
            throw new IllegalStateException("Invalid step");

        String otp = Help.createOTP(OTP_LENGTH);
        String hash = encoder.encode(otp);

        redisRepo.setField(userId, "new_email", newEmail);
        redisRepo.setField(userId, "new_otp_hash", hash);
        redisRepo.setField(userId, "new_verified", "false");
        redisRepo.setField(userId, "step", "VERIFY_NEW_EMAIL");

        emailService.send(newEmail, "OTP xác nhận email mới - dungcony", otp);

    }

    @Override
    public void verifyNewEmailOtp(int userId, String otp) {

        String step = redisRepo.getField(userId, "step");
        if (!"VERIFY_NEW_EMAIL".equals(step))
            throw new IllegalStateException("Invalid step");

        String hash = redisRepo.getField(userId, "new_otp_hash");
        if (!encoder.matches(otp, hash))
            throw new IllegalArgumentException("OTP invalid");

        redisRepo.setField(userId, "new_verified", "true");

        String newEmail = redisRepo.getField(userId, "new_email");

        Account acc = accountRepo.findById(userId).orElseThrow(TokenExpireException::new);
        acc.setEmail(newEmail);

        accountRepo.save(acc);

        redisRepo.delete(userId);
    }
}
