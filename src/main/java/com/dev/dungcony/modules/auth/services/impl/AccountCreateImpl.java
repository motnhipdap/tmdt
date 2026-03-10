package com.dev.dungcony.modules.auth.services.impl;

import com.dev.dungcony.modules.auth.entities.Account;
import com.dev.dungcony.modules.auth.repositories.AccountRepository;
import com.dev.dungcony.modules.auth.services.interfaces.AccountCreateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AccountCreateImpl implements AccountCreateService {

    private final AccountRepository accRepository;

    @Override
    public void createAccount(String email, String username, String password) {

        Account account = new Account();
        account.setEmail(email);
        account.setUsername(username);
        account.setPassword(password); // password đã được encode từ caller

        accRepository.save(account);
    }
}
