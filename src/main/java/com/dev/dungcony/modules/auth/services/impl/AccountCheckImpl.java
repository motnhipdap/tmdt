package com.dev.dungcony.modules.auth.services.impl;


import com.dev.dungcony.modules.auth.entities.Account;
import com.dev.dungcony.modules.auth.exceptions.InvalidUsernameOrPassword;
import com.dev.dungcony.modules.auth.exceptions.TokenExpireException;
import com.dev.dungcony.modules.auth.repositories.AccountRepository;
import com.dev.dungcony.modules.auth.services.interfaces.AccountCheckService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AccountCheckImpl implements AccountCheckService {
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
    public void emailAndUsernameIsTrue(int accId, String email, String username) {
        Account acc = accRepo.findById(accId).orElse(null);

        if (acc == null || !acc.getUsername().equals(username) || !acc.getEmail().equals(email))
            throw new InvalidUsernameOrPassword();
    }

    @Override
    public void checkPassword(int accId, String password) {
        Account acc = accRepo.findById(accId).orElseThrow(TokenExpireException::new);
        if (!passwordEncoder.matches(password, acc.getPassword()))
            throw new InvalidUsernameOrPassword();
    }
}
