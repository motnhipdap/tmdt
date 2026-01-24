package com.dev.dungcony.modules.authorization.services.impl;

import com.dev.dungcony.modules.authorization.dtos.LoginResult;
import com.dev.dungcony.modules.authorization.entities.Account;
import com.dev.dungcony.modules.authorization.enums.AccountEnum;
import com.dev.dungcony.modules.authorization.repositories.AccountRepository;
import com.dev.dungcony.modules.authorization.services.interfaces.AccountService;
import com.dev.dungcony.modules.authorization.services.interfaces.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {

    private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    @Autowired
    private AccountRepository accRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;

    @Override
    public LoginResult authenticate(String username, String password) {

        try {

            logger.info("username: {}", username);
            Account acc = accRepo.findByUsername(username).orElse(null);
            logger.info("acc: {}", acc);

            if (acc == null)
                return new LoginResult(AccountEnum.NOT_FOUND);
            logger.info("passwordEncoder: {}", passwordEncoder.encode(password));
            if (!passwordEncoder.matches(password, acc.getPassword()))
                return new LoginResult(AccountEnum.INCORRECT_PASSWORD);

            logger.info("password: {}", password);
            logger.info("acc.getPassword(): {}", acc.getPassword());

            String token = jwtService.generateToken(acc.getId(), acc.getUsername(), acc.getRole());
            logger.info("token: {}", token);

            return new LoginResult(token);

        } catch (Exception e) {
            logger.error("error: {}", e.getMessage());
            return new LoginResult(AccountEnum.FAILED);
        }
    }

    @Override
    public boolean existsByEmail(String email) {
        email = email.trim().toLowerCase();
        return accRepo.findByEmail(email).isPresent();
    }

    @Override
    public boolean existsByUsername(String username) {
        username = username.trim();
        return accRepo.findByUsername(username).isPresent();
    }

    @Override
    public AccountEnum createAccount(Account acc) {
        try {

            if (accRepo.existsByEmail(acc.getEmail()))
                return AccountEnum.EMAIL_EXISTS;
            if (accRepo.existsByUsername(acc.getUsername()))
                return AccountEnum.USERNAME_EXISTS;

            String hashPas = passwordEncoder.encode(acc.getPassword());
            acc.setPassword(hashPas);

            accRepo.save(acc);

            return AccountEnum.SUCCESS;

        } catch (Exception e) {
            logger.error("account created failed: {}", e.getMessage());
            return AccountEnum.FAILED;
        }
    }

    @Override
    public AccountEnum updateAccount(Account acc) {
        return null;
    }

    @Override
    public Account getAccountByEmail(String email) {
        return null;
    }

    @Override
    public Account getAccountByUsername(String username) {
        return null;
    }
}
