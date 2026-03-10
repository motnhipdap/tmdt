package com.dev.dungcony.modules.auth.controllers.store;

import com.dev.dungcony.commons.dtos.ApiRes;
import com.dev.dungcony.modules.auth.dtos.req.RegisReq;
import com.dev.dungcony.modules.auth.dtos.req.VerifyOtpReq;
import com.dev.dungcony.modules.auth.services.interfaces.AuthService;
import com.dev.dungcony.modules.auth.services.interfaces.VerifyOtpService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("v1/api/public/auth/regis")
@Tag(name = "Auth", description = "đăng ký tài khoản")
public class RegisController {

    private final AuthService authService;
    private final VerifyOtpService verifyOtpService;
    
    @Operation(summary = "Đăng ký tài khoản")
    @PostMapping("/")
    public ResponseEntity<ApiRes<Void>> register(@Valid @RequestBody RegisReq req) {
        authService.register(req);
        return ResponseEntity.ok()
                .body(ApiRes.success("register success"));
    }

    @Operation(summary = "verify otp và hoàn tất đăng ký")
    @PostMapping("/verify")
    public ResponseEntity<ApiRes<Void>> register(@Valid @RequestBody VerifyOtpReq req) {
        verifyOtpService.verifyOtpRegister(req);
        return ResponseEntity.ok()
                .body(ApiRes.success("register success"));
    }

}
