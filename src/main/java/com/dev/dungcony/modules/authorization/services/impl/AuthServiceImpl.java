package com.dev.dungcony.modules.authorization.services.impl;

import com.dev.dungcony.modules.authorization.config.JwtConfig;
import com.dev.dungcony.modules.authorization.dtos.requests.RegisReq;
import com.dev.dungcony.modules.authorization.dtos.responses.AccountRes;
import com.dev.dungcony.modules.authorization.dtos.responses.LoginRes;
import com.dev.dungcony.modules.authorization.dtos.responses.LoginResult;
import com.dev.dungcony.modules.authorization.entities.Account;
import com.dev.dungcony.modules.authorization.exceptions.EmailExistException;
import com.dev.dungcony.modules.authorization.exceptions.InvalidCredentialsException;
import com.dev.dungcony.modules.authorization.exceptions.UsernameExistsException;
import com.dev.dungcony.modules.authorization.repositories.AccountRepository;
import com.dev.dungcony.modules.authorization.services.interfaces.AuthService;
import com.dev.dungcony.modules.authorization.services.interfaces.JwtService;
import com.dev.dungcony.modules.authorization.services.interfaces.RefreshTokenService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final AccountRepository accRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    @Override
    public void register(RegisReq req) {
        if (accRepository.existsByEmail(req.email()))
            throw new EmailExistException();
        if (accRepository.existsByUsername(req.username()))
            throw new UsernameExistsException();

        Account account = new Account();
        account.setEmail(req.email());
        account.setUsername(req.username());
        account.setPassword(passwordEncoder.encode(req.password()));

        try {
            accRepository.save(account);
        } catch (DataIntegrityViolationException e) {
            throw new EmailExistException();
        }
    }

    @Override
    public LoginResult login(String username, String password) {

        Account acc = accRepository.findByUsername(username).orElse(null);

        if (acc == null || !passwordEncoder.matches(password, acc.getPassword()))
            throw new InvalidCredentialsException();

        String token = jwtService.generateToken(acc.getId(), acc.getUsername(), acc.getRole());
        String refreshToken = refreshTokenService.create(acc.getId());

        ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/auth/refresh")
                .maxAge(JwtConfig.refreshExpiration)
                .build();


        return new LoginResult(token, JwtConfig.headerPrefix, JwtConfig.expiration, refreshCookie.toString());
    }

    @Override
    public LoginRes refreshToken(String refreshToken) {

        AccountRes acc = refreshTokenService.verify(refreshToken);
        String token = jwtService.generateToken(acc.id(), acc.username(), acc.role());

        return new LoginRes(token);
    }
}
