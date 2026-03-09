package com.dev.dungcony.modules.auth.controllers.store;

import com.dev.dungcony.commons.dtos.ApiRes;
import com.dev.dungcony.modules.auth.dtos.req.LoginReq;
import com.dev.dungcony.modules.auth.dtos.req.RegisReq;
import com.dev.dungcony.modules.auth.dtos.res.LoginRes;
import com.dev.dungcony.modules.auth.dtos.res.LoginResult;
import com.dev.dungcony.modules.auth.services.interfaces.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("v1/api/auth")
@Tag(name = "Auth", description = "Đăng nhập, đăng ký, refresh token, logout")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Đăng nhập", description = "Trả về access token và set refresh token vào cookie")
    @PostMapping("/login")
    public ResponseEntity<ApiRes<LoginRes>> login(
            @Valid @RequestBody LoginReq loginReq,
            @RequestHeader("X-Device-Id") String deviceId) {
        log.info("Login req: {}", loginReq);

        LoginResult res = authService.login(loginReq.username(), loginReq.password(), deviceId);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, res.refreshToken())
                .body(ApiRes.success(
                        "login success",
                        new LoginRes(res.token(), res.expired())));
    }

    @Operation(summary = "Đăng ký tài khoản")
    @PostMapping("/register")
    public ResponseEntity<ApiRes<Void>> register(@Valid @RequestBody RegisReq req) {
        authService.register(req);
        return ResponseEntity.ok()
                .body(ApiRes.success("register success"));
    }

    @Operation(summary = "Refresh token", description = "Dùng refresh token từ cookie để lấy access token mới")
    @PostMapping("/refresh")
    public ResponseEntity<ApiRes<LoginRes>> refresh(
            @CookieValue("refresh_token") String token) {
        return ResponseEntity.ok()
                .body(ApiRes.success("refresh_success", authService.refreshToken(token)));
    }

    @Operation(summary = "Đăng xuất", description = "Xóa refresh token và device session")
    @PostMapping("/logout")
    public ResponseEntity<ApiRes<Void>> logout(
            @CookieValue("refresh_token") String token,
            @RequestHeader("X-Device-Id") String deviceId) {
        log.info("Logout token: {}", token);
        authService.logout(token, deviceId);
        return ResponseEntity.ok()
                .body(ApiRes.success("logout success"));
    }
}
