package com.dev.dungcony.modules.auth.services.impl;


import com.dev.dungcony.modules.auth.entities.Account;
import com.dev.dungcony.modules.auth.exceptions.InvalidCredentialsException;
import com.dev.dungcony.modules.auth.repositories.AccountRepository;
import com.dev.dungcony.modules.auth.services.interfaces.AccountCheckValidService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AccountCheckValidImpl implements AccountCheckValidService {
    private final AccountRepository accRepo;

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
        Account acc = accRepo.findById(accId).orElseThrow(
                () ->
                        new InvalidCredentialsException("email or username invalid"));
        if (!acc.getUsername().equals(username) || !acc.getEmail().equals(email))
            throw new InvalidCredentialsException("email or username invalid");
    }
}
