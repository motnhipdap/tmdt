package com.dev.dungcony.modules.authorization.services.impl;

import com.dev.dungcony.modules.authorization.dtos.responses.AccountRes;
import com.dev.dungcony.modules.authorization.entities.Account;
import com.dev.dungcony.modules.authorization.exceptions.TokenValidException;
import com.dev.dungcony.modules.authorization.repositories.AccountRepository;
import com.dev.dungcony.modules.authorization.services.interfaces.AccountService;
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
