package com.dev.dungcony.modules.auth.controllers;

import com.dev.dungcony.commons.dtos.ApiRes;
import com.dev.dungcony.modules.auth.dtos.requests.LoginReq;
import com.dev.dungcony.modules.auth.dtos.requests.RegisReq;
import com.dev.dungcony.modules.auth.dtos.responses.LoginRes;
import com.dev.dungcony.modules.auth.dtos.responses.LoginResult;
import com.dev.dungcony.modules.auth.services.interfaces.AuthService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("v1/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiRes<LoginRes>> login(
            @Valid @RequestBody LoginReq loginReq,
            @RequestHeader("X-Device-Id") String deviceId
    ) {
        log.info("Login req: {}", loginReq);

        LoginResult res = authService.login(loginReq.username(), loginReq.password(), deviceId);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, res.refreshToken())
                .body(ApiRes.success(
                        "login success",
                        new LoginRes(res.token(), res.expired())));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiRes<Void>> register(@Valid @RequestBody RegisReq req) {
        authService.register(req);
        return ResponseEntity.ok()
                .body(ApiRes.success("register success"));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiRes<LoginRes>> refresh(
            @CookieValue("refresh_token") String token) {
        return ResponseEntity.ok()
                .body(ApiRes.success("refresh_success", authService.refreshToken(token)));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiRes<Void>> logout(
            @CookieValue("refresh_token") String token,
            @RequestHeader("X-Device-Id") String deviceId
    ) {
        log.info("Logout token: {}", token);
        authService.logout(token, deviceId);
        return ResponseEntity.ok()
                .body(ApiRes.success("logout success"));
    }
}
