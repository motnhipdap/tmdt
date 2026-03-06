package com.dev.dungcony.modules.auth.controllers;

import com.dev.dungcony.commons.dtos.ApiRes;
import com.dev.dungcony.modules.auth.dtos.req.SendOtpReq;
import com.dev.dungcony.modules.auth.dtos.req.VerifyOtpReq;
import com.dev.dungcony.modules.auth.services.interfaces.OtpService;
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
@RequestMapping("v1/api/auth")
public class OtpController {

    private final OtpService otpService;

    @PostMapping("/send-regis-otp")
    public ResponseEntity<ApiRes<Void>> sendRegisOtp(@Valid @RequestBody SendOtpReq req) {
        otpService.send(req.email(), req.otpType());
        return ResponseEntity.ok()
                .body(ApiRes.success("send otp successfully"));
    }

    @PostMapping("/send-reset-password-otp")
    public ResponseEntity<ApiRes<Void>> sendResetPasswordOtp(@Valid @RequestBody SendOtpReq req) {
        otpService.send(req.email(), req.otpType());
        return ResponseEntity.ok()
                .body(ApiRes.success("send otp successfully"));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<ApiRes<Boolean>> verifyOtp(@Valid @RequestBody VerifyOtpReq req) {
        boolean verify = otpService.verifyOTP(req);
        return ResponseEntity.ok()
                .body(ApiRes.success("otp verify res", verify));
    }
}
