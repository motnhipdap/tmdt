package com.dev.dungcony.modules.authorization.services.impl;

import com.dev.dungcony.modules.authorization.dtos.requests.UpdateEmailReq;
import com.dev.dungcony.modules.authorization.dtos.responses.AccountRes;
import com.dev.dungcony.modules.authorization.entities.Account;
import com.dev.dungcony.modules.authorization.exceptions.TokenValidException;
import com.dev.dungcony.modules.authorization.repositories.AccountRepository;
import com.dev.dungcony.modules.authorization.services.interfaces.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AccountServiceImpl implements AccountService {

    private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    private final AccountRepository accRepo;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final OTPService otpService;
    private final JwtService jwtService;
    private final RedisService redisService;

    @Override
    public boolean existsByEmail(String email) {
        return accRepo.existsByEmail(email);
    }

    @Override
    public boolean existsByUsername(String username) {
        return accRepo.existsByUsername(username);
    }

    @Override
    @Transactional
    public void updateEmail(int id, UpdateEmailReq req) {
        Account acc = accRepo.findById(id).orElseThrow(TokenValidException::new);

        String email = acc.getEmail();
        ChangeEmail(id, email, req.newEmail());

    }

    public void ChangeEmail(int id, String oldEmail, String newEmail) {
        String old_token = jwtService.generateToken(id, oldEmail);
        String new_token = jwtService.generateToken(id, newEmail);
        String key1 = "user:" + id + ":" + oldEmail;
        String key2 = "user:" + id + ":" + newEmail;

        redisService.cache(key1, old_token);
        redisService.cache(key2, new_token);

        emailService.sendNewEmail(newEmail, new_token);
        emailService.sendOldEmail(oldEmail, old_token);
    }

    @Override
    @Transactional
    public boolean updatePassword(int id, String oldPassword, String newPassword) {
        Account acc = accRepo.findById(id).orElseThrow(TokenValidException::new);
        if (!passwordEncoder.matches(oldPassword, acc.getPassword()))
            return false;
        acc.setPassword(passwordEncoder.encode(newPassword));
        accRepo.save(acc);
        return true;
    }

    @Override
    public AccountRes getProfileById(int id) {
        Account acc = accRepo.findById(id).orElseThrow(TokenValidException::new);
        return new AccountRes(
                acc.getId(),
                acc.getEmail(),
                acc.getUsername(),
                acc.getPhone(),
                acc.getStatus(),
                acc.getRole(),
                acc.getCreatedAt());
    }
}
