package com.dev.dungcony.modules.authorization.controllers;

import com.dev.dungcony.commons.dtos.ApiRes;
import com.dev.dungcony.modules.authorization.helpers.AccountDetails;
import com.dev.dungcony.modules.authorization.services.interfaces.EmailChangeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("v1/api/email/update")
public class EmailUpdateController {
    private static final Logger logger = LoggerFactory.getLogger(EmailUpdateController.class);

    private EmailChangeService emailChangeService;
    private PasswordEncoder passwordEncoder;

    @PostMapping("/send-old-email")
    public ResponseEntity<ApiRes<Void>> updateReq(
            @AuthenticationPrincipal AccountDetails detail
    ) {
        emailChangeService.startChangeEmail(detail.getId());
        return ResponseEntity.ok()
                .body(ApiRes.success("send otp req update email successfully"));
    }

    @PostMapping("/verify-old-email")
    public ResponseEntity<ApiRes<Void>> verifyOldEmail(
            @AuthenticationPrincipal AccountDetails detail,
            @Valid @RequestBody String otp
    ) {
        emailChangeService.verifyOldEmailOtp(detail.getId(), otp);
        return ResponseEntity.ok()
                .body(ApiRes.success("successfully"));
    }

    @PostMapping("/send-new-email")
    public ResponseEntity<ApiRes<Void>> sendNewEmail(
            @AuthenticationPrincipal AccountDetails detail,
            @Valid @RequestBody String newEmail
    ) {
        emailChangeService.submitNewEmail(detail.getId(), newEmail);
        return ResponseEntity.ok()
                .body(ApiRes.success("send to email successfully"));
    }

    @PutMapping("/success")
    public ResponseEntity<ApiRes<Void>> success(
            @AuthenticationPrincipal AccountDetails detail,
            @Valid @RequestBody String otp
    ) {
        emailChangeService.verifyNewEmailOtp(detail.getId(), otp);
        return ResponseEntity.ok()
                .body(ApiRes.success("email update successfully"));
    }

}
