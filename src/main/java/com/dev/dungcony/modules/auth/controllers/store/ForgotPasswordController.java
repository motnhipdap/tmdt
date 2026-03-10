package com.dev.dungcony.modules.auth.controllers.store;

import com.dev.dungcony.commons.dtos.ApiRes;
import com.dev.dungcony.modules.auth.services.interfaces.ForgotPasswordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("v1/api/public/auth/forgot-password")
@Tag(name = "Auth", description = "Quên mật khẩu")
public class ForgotPasswordController {

    private final ForgotPasswordService forgotPasswordService;

    @Operation(summary = "Quên mật khẩu", description = "Gửi mật khẩu mới tới email đã đăng ký")
    @PostMapping
    public ResponseEntity<ApiRes<Void>> forgotPassword(
            @RequestParam @NotBlank @Email String email) {
        log.info("Forgot password request for email: {}", email);
        forgotPasswordService.forgotPassword(email);
        return ResponseEntity.ok()
                .body(ApiRes.success("New password sent to your email"));
    }
}

