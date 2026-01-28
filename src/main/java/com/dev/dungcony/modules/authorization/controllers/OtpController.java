package com.dev.dungcony.modules.authorization.controllers;

import com.dev.dungcony.commons.dtos.ApiRes;
import com.dev.dungcony.modules.authorization.dtos.requests.OtpReq;
import com.dev.dungcony.modules.authorization.dtos.requests.VerifyOtpReq;
import com.dev.dungcony.modules.authorization.services.interfaces.EmailService;
import com.dev.dungcony.modules.authorization.services.interfaces.OTPService;
import com.dev.dungcony.modules.authorization.services.interfaces.RedisService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/api/auth")
public class OtpController {

    private static final Logger logger = LoggerFactory.getLogger(OtpController.class);
    private final OTPService otpService;
    private final EmailService emailService;
    private final RedisService redisService;

    public OtpController(OTPService otpService, EmailService emailService, RedisService redisService) {
        this.otpService = otpService;
        this.emailService = emailService;
        this.redisService = redisService;
    }

    @PostMapping("/send-otp")
    public ResponseEntity<ApiRes<Void>> sendOtp(@Valid @RequestBody OtpReq req) {
        otpService.sendOtp(req);
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
