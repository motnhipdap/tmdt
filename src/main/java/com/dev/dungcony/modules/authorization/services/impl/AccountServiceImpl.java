package com.dev.dungcony.modules.authorization.services.impl;

import com.dev.dungcony.modules.authorization.dtos.AccountResult;
import com.dev.dungcony.modules.authorization.dtos.requests.UpdatePasswordReq;
import com.dev.dungcony.modules.authorization.dtos.responses.AccountDetail;
import com.dev.dungcony.modules.authorization.dtos.responses.LoginRes;
import com.dev.dungcony.modules.authorization.entities.Account;
import com.dev.dungcony.modules.authorization.enums.AccountEnum;
import com.dev.dungcony.modules.authorization.repositories.AccountRepository;
import com.dev.dungcony.modules.authorization.services.interfaces.AccountService;
import com.dev.dungcony.modules.authorization.services.interfaces.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {

    private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    private final AccountRepository accRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AccountServiceImpl(AccountRepository accRepo, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.accRepo = accRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public AccountResult<LoginRes> authenticate(String username, String password) {

        try {

            logger.info("username: {}", username);
            Account acc = accRepo.findByUsername(username).orElse(null);
            logger.info("acc: {}", acc);

            if (acc == null)
                return AccountResult.error(AccountEnum.NOT_FOUND);
            logger.info("passwordEncoder: {}", passwordEncoder.encode(password));
            if (!passwordEncoder.matches(password, acc.getPassword()))
                return AccountResult.error(AccountEnum.INCORRECT_PASSWORD);

            logger.info("password: {}", password);
            logger.info("acc.getPassword(): {}", acc.getPassword());

            String token = jwtService.generateToken(acc.getId(), acc.getUsername(), acc.getRole());
            logger.info("token: {}", token);

            return AccountResult.success(new LoginRes(token));

        } catch (Exception e) {
            logger.error("error: {}", e.getMessage());
            return AccountResult.error(AccountEnum.FAILED);
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
    public AccountResult<Void> createAccount(Account acc) {
        try {

            if (accRepo.existsByEmail(acc.getEmail()))
                return AccountResult.error(AccountEnum.EMAIL_EXISTS);
            if (accRepo.existsByUsername(acc.getUsername()))
                return AccountResult.error(AccountEnum.USERNAME_EXISTS);

            String hashPas = passwordEncoder.encode(acc.getPassword());
            acc.setPassword(hashPas);

            accRepo.save(acc);

            return AccountResult.success();

        } catch (Exception e) {
            logger.error("account created failed: {}", e.getMessage());
            return AccountResult.error(AccountEnum.FAILED);
        }
    }

    @Override
    public AccountResult<Void> updatePassword(String username, UpdatePasswordReq req) {
        try {
            Account acc = accRepo.findByUsername(username).orElse(null);
            if (acc == null)
                return AccountResult.error(AccountEnum.NOT_FOUND);

            if (!passwordEncoder.matches(req.oldPass(), acc.getPassword()))
                return AccountResult.error(AccountEnum.INCORRECT_PASSWORD);

            acc.setPassword(passwordEncoder.encode(req.newPass()));

            accRepo.save(acc);

            return AccountResult.success();

        } catch (Exception e) {
            return AccountResult.error(AccountEnum.FAILED);
        }
    }

    @Override
    public AccountResult<AccountDetail> getProfileById(int id) {
        try {
            Account acc = accRepo.findById(id).orElse(null);
            if (acc == null)
                return AccountResult.error(AccountEnum.NOT_FOUND);
            AccountDetail accD = new AccountDetail(
                    acc.getEmail(),
                    acc.getUsername(),
                    acc.getPhone(),
                    acc.getStatus(),
                    acc.getCreatedAt());

            return AccountResult.success(accD);
        } catch (Exception e) {
            logger.error("getProfileById failed: {}", e.getMessage());
            return AccountResult.error(AccountEnum.FAILED);
        }
    }

    @Override
    public AccountResult<Void> updateAccount(Account acc) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateAccount'");
    }
}
