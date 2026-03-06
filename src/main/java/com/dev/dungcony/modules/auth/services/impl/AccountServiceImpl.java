package com.dev.dungcony.modules.auth.services.impl;

import com.dev.dungcony.modules.auth.dtos.res.AccountRes;
import com.dev.dungcony.modules.auth.entities.Account;
import com.dev.dungcony.modules.auth.exceptions.TokenValidException;
import com.dev.dungcony.modules.auth.repositories.AccountRepository;
import com.dev.dungcony.modules.auth.services.interfaces.AccountService;
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
    public boolean updatePassword(int id, String oldPassword, String newPassword) {
        Account acc = accRepo.findById(id).orElseThrow(TokenValidException::new);
        if (!passwordEncoder.matches(oldPassword, acc.getPassword()))
            return false;
        acc.setPassword(passwordEncoder.encode(newPassword));
        accRepo.save(acc);
        log.info("Password updated for account: {}", acc.getUsername());
        return true;
    }

    @Override
    public AccountRes getProfileById(int id) {
        Account acc = accRepo.findById(id).orElseThrow(TokenValidException::new);
        log.info("Retrieving profile for account: {}", acc.getUsername());
        return new AccountRes(
                acc.getId(),
                acc.getEmail(),
                acc.getUsername(),
                acc.getStatus(),
                acc.getRole().name(),
                acc.getCreatedAt());
    }
}
