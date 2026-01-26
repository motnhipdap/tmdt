package com.dev.dungcony.modules.authorization.services.impl;

import com.dev.dungcony.modules.authorization.dtos.requests.LoginReq;
import com.dev.dungcony.modules.authorization.dtos.requests.RegisReq;
import com.dev.dungcony.modules.authorization.dtos.responses.LoginRes;
import com.dev.dungcony.modules.authorization.entities.Account;
import com.dev.dungcony.modules.authorization.exceptions.EmailExistException;
import com.dev.dungcony.modules.authorization.exceptions.InvalidCredentialsException;
import com.dev.dungcony.modules.authorization.exceptions.UsernameExistsException;
import com.dev.dungcony.modules.authorization.repositories.AccountRepository;
import com.dev.dungcony.modules.authorization.services.interfaces.AuthService;
import com.dev.dungcony.modules.authorization.services.interfaces.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AccountRepository accRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    @Override
    public void register(RegisReq regisReq) {
        if (accRepository.existsByEmail(regisReq.email()))
            throw new EmailExistException();
        if (accRepository.existsByUsername(regisReq.username()))
            throw new UsernameExistsException();

        Account account = new Account();
        account.setEmail(regisReq.email());
        account.setUsername(regisReq.username());
        account.setPassword(passwordEncoder.encode(regisReq.password()));

        accRepository.save(account);
    }

    @Override
    public LoginRes login(LoginReq loginReq) {

        Account acc = accRepository.findByUsername(loginReq.username()).orElse(null);
        if (acc == null || passwordEncoder.matches(loginReq.password(), acc.getPassword()))
            throw new InvalidCredentialsException();

        String token = jwtService.generateToken(acc.getId(), acc.getUsername(), acc.getRole());

        return new LoginRes(token);
    }
}
