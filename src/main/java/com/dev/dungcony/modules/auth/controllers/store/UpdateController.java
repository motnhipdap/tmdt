package com.dev.dungcony.modules.auth.controllers.store;

import com.dev.dungcony.commons.dtos.AccountDetails;
import com.dev.dungcony.commons.dtos.ApiRes;
import com.dev.dungcony.modules.auth.dtos.req.SendOtpChangeEmailReq;
import com.dev.dungcony.modules.auth.dtos.req.UpdatePasswordReq;
import com.dev.dungcony.modules.auth.dtos.req.VerifyOtpEmailChangeReq;
import com.dev.dungcony.modules.auth.dtos.req.VerifyOtpReq;
import com.dev.dungcony.modules.auth.services.interfaces.AccountCheckService;
import com.dev.dungcony.modules.auth.services.interfaces.AccountUpdateService;
import com.dev.dungcony.modules.auth.services.interfaces.SendOtpService;
import com.dev.dungcony.modules.auth.services.interfaces.VerifyOtpService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("v1/api/account/update")
@Tag(name = "Update", description = "Các API cập nhật thông tin tài khoản như email, password")
public class UpdateController {

    private final AccountUpdateService accountUpdateService;

    private final SendOtpService sendOtpService;
    private final VerifyOtpService verifyOtpService;
    private final AccountCheckService accountCheckService;

    @Operation(summary = "Gửi OTP tới email cũ")
    @PostMapping("/email/send-otp")
    public ResponseEntity<ApiRes<Void>> sendOtpChangeEmail(
            @AuthenticationPrincipal AccountDetails detail,
            @RequestBody SendOtpChangeEmailReq req) {
        sendOtpService.sendOtpChangeEmail(detail.getId(), req);
        return ResponseEntity.ok()
                .body(ApiRes.success("send otp req update email successfully"));
    }

    @Operation(summary = "Xác nhận OTP email cũ và thay email mới")
    @PostMapping("/email/verify-otp")
    public ResponseEntity<ApiRes<Void>> verifyOldEmail(
            @AuthenticationPrincipal AccountDetails detail,
            @Valid @RequestBody VerifyOtpEmailChangeReq req) {
        verifyOtpService.verifyOtpEmailChange(detail.getId(), req);
        return ResponseEntity.ok()
                .body(ApiRes.success("successfully"));
    }

    @Operation(summary = "update password")
    @PostMapping("/password")
    public ResponseEntity<ApiRes<Void>> verifyOldEmail(
            @AuthenticationPrincipal AccountDetails detail,
            @Valid @RequestBody UpdatePasswordReq req) {
        accountUpdateService.updatePassword(detail.getId(), req.oldPass(), req.newPass());
        return ResponseEntity.ok()
                .body(ApiRes.success("successfully"));
    }
}
