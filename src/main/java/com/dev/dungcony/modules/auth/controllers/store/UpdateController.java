package com.dev.dungcony.modules.auth.controllers.store;

import com.dev.dungcony.commons.dtos.AccountDetails;
import com.dev.dungcony.commons.dtos.ApiRes;
import com.dev.dungcony.modules.auth.dtos.req.UpdatePasswordReq;
import com.dev.dungcony.modules.auth.services.interfaces.AccountUpdateService;
import com.dev.dungcony.modules.auth.services.interfaces.SendOtpService;
import com.dev.dungcony.modules.auth.services.interfaces.VerifyOtpService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("v1/api/account/update")
@Tag(name = "Update", description = "Các API cập nhật thông tin tài khoản như email, password")
public class UpdateController {

    private final AccountUpdateService accountUpdateService;
    private final SendOtpService sendOtpService;
    private final VerifyOtpService verifyOtpService;

    @Operation(summary = "Gửi OTP tới email cũ")
    @PostMapping("/email/send-otp")
    public ResponseEntity<ApiRes<Void>> sendOtpChangeEmail(
            @AuthenticationPrincipal AccountDetails detail,
            @RequestParam @NotBlank @Email String oldEmail) {
        sendOtpService.sendOtpChangeEmail(detail.getId(), detail.getUsername(), oldEmail);
        return ResponseEntity.ok()
                .body(ApiRes.success("send otp req update email successfully"));
    }

    @Operation(summary = "Xác nhận OTP và thay email mới")
    @PostMapping("/email/verify-otp")


    public ResponseEntity<ApiRes<Void>> confirmEmailChange(
            @AuthenticationPrincipal AccountDetails detail,
            @RequestParam @NotBlank @Email String newEmail,
            @RequestParam @NotBlank String otp) {
        verifyOtpService.verifyOtpEmailChange(detail.getId(), detail.getUsername(), newEmail, otp);
        return ResponseEntity.ok()
                .body(ApiRes.success("successfully"));
    }

    @Operation(summary = "Cập nhật mật khẩu")
    @PostMapping("/password")
    public ResponseEntity<ApiRes<Void>> updatePass(
            @AuthenticationPrincipal AccountDetails detail,
            @Valid @RequestBody UpdatePasswordReq req) {
        accountUpdateService.updatePassword(detail.getId(), req.oldPass(), req.newPass());
        return ResponseEntity.ok()
                .body(ApiRes.success("successfully"));
    }
}
