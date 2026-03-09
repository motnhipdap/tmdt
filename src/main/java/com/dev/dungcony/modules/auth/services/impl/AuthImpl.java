package com.dev.dungcony.modules.auth.services.impl;

import com.dev.dungcony.commons.exceptions.ConflictException;
import com.dev.dungcony.modules.auth.config.JwtConfig;
import com.dev.dungcony.modules.auth.dtos.AccDto;
import com.dev.dungcony.modules.auth.dtos.req.RegisReq;
import com.dev.dungcony.modules.auth.dtos.res.AccountRes;
import com.dev.dungcony.modules.auth.dtos.res.LoginRes;
import com.dev.dungcony.modules.auth.dtos.res.LoginResult;
import com.dev.dungcony.modules.auth.entities.Account;
import com.dev.dungcony.modules.auth.exceptions.EmailExistException;
import com.dev.dungcony.modules.auth.exceptions.InvalidCredentialsException;
import com.dev.dungcony.modules.auth.exceptions.UsernameExistsException;
import com.dev.dungcony.modules.auth.repositories.AccountRepository;
import com.dev.dungcony.modules.auth.services.interfaces.AuthService;
import com.dev.dungcony.modules.auth.services.interfaces.JwtService;
import com.dev.dungcony.modules.auth.services.interfaces.RefreshTokenService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthImpl implements AuthService {

    private final AccountRepository accRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final JwtConfig jwtConfig;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    @Override
    public AccountRes register(RegisReq req) {

        Account acc = accRepository.findByEmail(req.email()).orElse(null);
        if (acc != null) {
            if (acc.getVerify())
                throw new EmailExistException();
        }

        acc = accRepository.findByUsername(req.username()).orElse(null);
        if (acc != null) {
            if (acc.getVerify())
                throw new UsernameExistsException();
        }

        Account account = new Account();
        account.setEmail(req.email());
        account.setUsername(req.username());
        account.setPassword(passwordEncoder.encode(req.password()));

        try {
            accRepository.save(account);
            return new AccountRes(
                    account.getEmail(),
                    account.getUsername(),
                    account.getStatus(),
                    account.getRole(),
                    account.getVerify(),
                    account.getCreatedAt()
            );
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            // Race condition: another request registered with same email/username between
            // our check and save
            String msg = e.getMessage() != null ? e.getMessage().toLowerCase() : "";
            if (msg.contains("username"))
                throw new UsernameExistsException();
            throw new ConflictException("email or username already taken");
        }
    }

    @Override
    public LoginResult login(String username, String password, String deviceId) {

        Account acc = accRepository.findByUsername(username).orElse(null);

        if (acc == null || !passwordEncoder.matches(password, acc.getPassword()))
            throw new InvalidCredentialsException();

        String token = jwtService.generateToken(acc.getId(), acc.getUsername(), acc.getRole());
        String refreshToken = refreshTokenService.create(acc.getId(), deviceId);

        ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/v1/api/auth/refresh")
                .maxAge(jwtConfig.getRefreshExpiration())
                .build();

        return new LoginResult(token, JwtConfig.headerPrefix, jwtConfig.getExpiration(), refreshCookie.toString());
    }

    @Override
    public LoginRes refreshToken(String refreshToken) {
        AccDto acc = refreshTokenService.verify(refreshToken);
        String token = jwtService.generateToken(acc.id(), acc.username(), acc.role());

        return new LoginRes(token, jwtConfig.getExpiration());
    }

    @Override
    public void logout(String refreshToken, String deviceId) {
        refreshTokenService.revoke(refreshToken, deviceId);
    }
}
