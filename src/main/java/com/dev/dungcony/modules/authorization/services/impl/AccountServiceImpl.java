package com.dev.dungcony.modules.authorization.services.impl;

import com.dev.dungcony.modules.authorization.dtos.AccountResult;
import com.dev.dungcony.modules.authorization.dtos.LoginResult;
import com.dev.dungcony.modules.authorization.dtos.requests.UpdatePasswordReq;
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
    public AccountResult createAccount(Account acc) {
        try {

            if (accRepo.existsByEmail(acc.getEmail()))
                return new AccountResult(AccountEnum.EMAIL_EXISTS);
            if (accRepo.existsByUsername(acc.getUsername()))
                return new AccountResult(AccountEnum.USERNAME_EXISTS);

            String hashPas = passwordEncoder.encode(acc.getPassword());
            acc.setPassword(hashPas);

            accRepo.save(acc);

            return new AccountResult(AccountEnum.SUCCESS);

        } catch (Exception e) {
            logger.error("account created failed: {}", e.getMessage());
            return new AccountResult(AccountEnum.FAILED);
        }
    }

    @Override
    public AccountResult updatePassword(String username, UpdatePasswordReq req) {
        try {
            Account acc = accRepo.findByUsername(username).orElse(null);
            if (acc == null)
                return new AccountResult(AccountEnum.NOT_FOUND);

            if (!passwordEncoder.matches(req.getOldPass(), acc.getPassword()))
                return new AccountResult(AccountEnum.INCORRECT_PASSWORD);

            acc.setPassword(passwordEncoder.encode(req.getNewPass()));

            accRepo.save(acc);

            return new AccountResult(AccountEnum.SUCCESS);

        } catch (Exception e) {
            return new AccountResult(AccountEnum.FAILED);
        }
    }

    @Override
    public AccountResult updateAccount(Account acc) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateAccount'");
    }
}
