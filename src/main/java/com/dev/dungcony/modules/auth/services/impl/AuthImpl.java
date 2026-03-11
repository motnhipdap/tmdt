package com.dev.dungcony.modules.auth.services.impl;

import com.dev.dungcony.modules.auth.config.JwtConfig;
import com.dev.dungcony.modules.auth.dtos.AccDto;
import com.dev.dungcony.modules.auth.dtos.req.RegisReq;
import com.dev.dungcony.modules.auth.dtos.res.AcessTokenRes;
import com.dev.dungcony.modules.auth.dtos.res.LoginResult;
import com.dev.dungcony.modules.auth.exceptions.InvalidUsernameOrPassword;
import com.dev.dungcony.modules.auth.enums.Status;
import com.dev.dungcony.modules.auth.services.interfaces.*;
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

    private final AccountGetService accGetService;
    private final AccountCheckService accountCheckService;
    private final AccountCreateService accountCreateService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final JwtConfig jwtConfig;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    @Override
    public void register(RegisReq req) {

        accountCheckService.existsByEmail(req.email());
        accountCheckService.existsByUsername(req.username());

        accountCreateService.createAccount(req.email(), req.username(), passwordEncoder.encode(req.password()));
    }

    @Override
    public LoginResult login(String username, String password, String deviceId) {

        AccDto acc = accGetService.getByUsername(username);

        if (!passwordEncoder.matches(password, acc.password()))
            throw new InvalidUsernameOrPassword();

        if (!acc.verify())
            throw new InvalidUsernameOrPassword();

        if (acc.status() != Status.ACTIVE)
            throw new InvalidUsernameOrPassword(); // account bị khóa

        String acessToken = jwtService.generateToken(acc.id(), acc.username(), acc.role());
        String refreshToken = refreshTokenService.create(acc.id(), deviceId);

        ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/v1/api/auth/refresh")
                .maxAge(jwtConfig.getRefreshExpiration())
                .build();

        return new LoginResult(acessToken, JwtConfig.headerPrefix, jwtConfig.getExpiration(), refreshCookie.toString());
    }

    @Override
    public AcessTokenRes refreshToken(String refreshToken) {
        AccDto acc = refreshTokenService.verify(refreshToken);
        String token = jwtService.generateToken(acc.id(), acc.username(), acc.role());

        return new AcessTokenRes(token, jwtConfig.getExpiration());
    }

    @Override
    public void logout(String refreshToken, String deviceId) {
        refreshTokenService.revoke(refreshToken, deviceId);
    }
}
