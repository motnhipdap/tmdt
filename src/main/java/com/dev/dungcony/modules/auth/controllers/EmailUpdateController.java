package com.dev.dungcony.modules.auth.controllers;

import com.dev.dungcony.commons.dtos.AccountDetails;
import com.dev.dungcony.commons.dtos.ApiRes;
import com.dev.dungcony.modules.auth.services.interfaces.EmailChangeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("v1/api/email/update")
@Tag(name = "Email Update", description = "Quy trình đổi email: gửi OTP email cũ → xác nhận → gửi OTP email mới → xác nhận")
public class EmailUpdateController {
    private static final Logger logger = LoggerFactory.getLogger(EmailUpdateController.class);

    private final EmailChangeService emailChangeService;

    @Operation(summary = "Bước 1: Gửi OTP tới email cũ")
    @PostMapping("/send-old-email")
    public ResponseEntity<ApiRes<Void>> updateReq(
            @AuthenticationPrincipal AccountDetails detail) {
        emailChangeService.startChangeEmail(detail.getId());
        return ResponseEntity.ok()
                .body(ApiRes.success("send otp req update email successfully"));
    }

    @Operation(summary = "Bước 2: Xác nhận OTP email cũ")
    @PostMapping("/verify-old-email")
    public ResponseEntity<ApiRes<Void>> verifyOldEmail(
            @AuthenticationPrincipal AccountDetails detail,
            @Valid @RequestBody String otp) {
        emailChangeService.verifyOldEmailOtp(detail.getId(), otp);
        return ResponseEntity.ok()
                .body(ApiRes.success("successfully"));
    }

    @Operation(summary = "Bước 3: Gửi OTP tới email mới")
    @PostMapping("/send-new-email")
    public ResponseEntity<ApiRes<Void>> sendNewEmail(
            @AuthenticationPrincipal AccountDetails detail,
            @Valid @RequestBody String newEmail) {
        emailChangeService.submitNewEmail(detail.getId(), newEmail);
        return ResponseEntity.ok()
                .body(ApiRes.success("send to email successfully"));
    }

    @Operation(summary = "Bước 4: Xác nhận OTP email mới → hoàn tất đổi email")
    @PutMapping("/success")
    public ResponseEntity<ApiRes<Void>> success(
            @AuthenticationPrincipal AccountDetails detail,
            @Valid @RequestBody String otp) {
        emailChangeService.verifyNewEmailOtp(detail.getId(), otp);
        return ResponseEntity.ok()
                .body(ApiRes.success("email update successfully"));
    }

}
