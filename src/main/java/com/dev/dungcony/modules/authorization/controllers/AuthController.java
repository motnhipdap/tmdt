package com.dev.dungcony.modules.authorization.controllers;

import com.dev.dungcony.commons.dtos.ApiRes;
import com.dev.dungcony.modules.authorization.dtos.requests.LoginReq;
import com.dev.dungcony.modules.authorization.dtos.requests.RegisReq;
import com.dev.dungcony.modules.authorization.dtos.responses.LoginRes;
import com.dev.dungcony.modules.authorization.dtos.responses.LoginResult;
import com.dev.dungcony.modules.authorization.services.interfaces.AuthService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/api/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiRes<LoginRes>> login(@Valid @RequestBody LoginReq loginReq) {

        LoginResult res = authService.login(loginReq.username(), loginReq.password());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, res.refreshToken())
                .body(ApiRes.success(
                        "login success",
                        new LoginRes(res.token())
                ));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiRes<Void>> register(@Valid @RequestBody RegisReq req) {
        authService.register(req);
        return ResponseEntity.ok()
                .body(ApiRes.success("register success"));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiRes<LoginRes>> refresh(
            @CookieValue("refresh_token") String token
    ) {
        return ResponseEntity.ok()
                .body(ApiRes.success("refresh_success", authService.refreshToken(token)));
    }
}
