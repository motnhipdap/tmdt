package com.dev.dungcony.modules.auth.services.impl;

import com.dev.dungcony.modules.auth.config.Env;
import com.dev.dungcony.modules.auth.dtos.res.AccountRes;
import com.dev.dungcony.modules.auth.entities.Account;
import com.dev.dungcony.modules.auth.exceptions.TokenExpireException;
import com.dev.dungcony.modules.auth.exceptions.TokenValidException;
import com.dev.dungcony.modules.auth.helpers.Generate;
import com.dev.dungcony.modules.auth.repositories.AccountRepository;
import com.dev.dungcony.modules.auth.repositories.EmailChangeRedisRepository;
import com.dev.dungcony.modules.auth.services.interfaces.EmailService;
import com.dev.dungcony.modules.auth.services.interfaces.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accRepo;
    private final PasswordEncoder passwordEncoder;
    private final EmailChangeRedisRepository redisRepo;
    private final EmailService emailService;
    private final RedisService redisService;
    private final Generate generate;

    private final int OTP_LENGTH = 6;

    // ========================= Profile =========================

    @Override
    public AccountRes getProfileById(int id) {
        Account acc = accRepo.findById(id).orElseThrow(TokenValidException::new);
        log.info("Retrieving profile for account: {}", acc.getUsername());
        return new AccountRes(
                acc.getEmail(),
                acc.getUsername(),
                acc.getStatus(),
                acc.getRole(),
                acc.getVerify(),
                acc.getCreatedAt());
    }

    // ========================= Check =========================

    // ========================= Email Change =========================

    @Override
    @Transactional
    public void startChangeEmail(int userId) {
        if (redisRepo.exists(userId))
            throw new IllegalStateException("Email change in progress");

        Account acc = accRepo.findById(userId).orElseThrow(TokenValidException::new);

        String otp = generate.otp(OTP_LENGTH);
        String hash = passwordEncoder.encode(otp);

        redisRepo.createRequest(
                userId,
                acc.getEmail(),
                hash,
                Env.expiration_change_email);

        emailService.send(acc.getEmail(), "OTP yêu cầu thay đổi email - dungcony", otp);
        log.info("Email change request sent to: {}", acc.getEmail());
    }

    @Override
    public void verifyOldEmailOtp(int userId, String otp) {
        String step = redisRepo.getField(userId, "step");
        if (!"VERIFY_OLD_EMAIL".equals(step))
            throw new IllegalStateException("Invalid step");

        String hash = redisRepo.getField(userId, "old_otp_hash");
        if (!passwordEncoder.matches(otp, hash))
            throw new IllegalArgumentException("OTP invalid");

        redisRepo.setField(userId, "old_verified", "true");
        redisRepo.setField(userId, "step", "INPUT_NEW_EMAIL");
    }

    @Override
    public void submitNewEmail(int userId, String newEmail) {
        String step = redisRepo.getField(userId, "step");
        if (!"INPUT_NEW_EMAIL".equals(step))
            throw new IllegalStateException("Invalid step");

        String otp = generate.otp(OTP_LENGTH);
        String hash = passwordEncoder.encode(otp);

        redisRepo.setField(userId, "new_email", newEmail);
        redisRepo.setField(userId, "new_otp_hash", hash);
        redisRepo.setField(userId, "new_verified", "false");
        redisRepo.setField(userId, "step", "VERIFY_NEW_EMAIL");

        emailService.send(newEmail, "OTP xác nhận email mới - dungcony", otp);
    }

    @Override
    @Transactional
    public void verifyNewEmailOtp(int userId, String otp) {
        String step = redisRepo.getField(userId, "step");
        if (!"VERIFY_NEW_EMAIL".equals(step))
            throw new IllegalStateException("Invalid step");

        String hash = redisRepo.getField(userId, "new_otp_hash");
        if (!passwordEncoder.matches(otp, hash))
            throw new IllegalArgumentException("OTP invalid");

        redisRepo.setField(userId, "new_verified", "true");

        String newEmail = redisRepo.getField(userId, "new_email");
        Account acc = accRepo.findById(userId).orElseThrow(TokenExpireException::new);
        acc.setEmail(newEmail);
        accRepo.save(acc);

        redisRepo.delete(userId);
    }

    @Override
    public void verifyEmailChange(String email) {
        redisService.
    }

    @Override
    public void verifyEmailRegis(String email) {
        int cntRows = accRepo.verifyEmail(email);
        if (cntRows == 0)
            throw new IllegalArgumentException("Email not found");
    }
}
