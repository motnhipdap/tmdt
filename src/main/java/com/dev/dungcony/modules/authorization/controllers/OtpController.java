package com.dev.dungcony.modules.authorization.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dev.dungcony.modules.authorization.dtos.requests.OtpReq;
import com.dev.dungcony.modules.authorization.dtos.requests.OtpVerifyReq;
import com.dev.dungcony.modules.authorization.dtos.responses.ApiRes;
import com.dev.dungcony.modules.authorization.services.interfaces.EmailService;
import com.dev.dungcony.modules.authorization.services.interfaces.OTPService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("v1/api/auth")
public class OtpController {

    private static final Logger logger = LoggerFactory.getLogger(OtpController.class);

    private final OTPService otpService;

    private final EmailService emailService;

    public OtpController(OTPService otpService, EmailService emailService) {
        this.otpService = otpService;
        this.emailService = emailService;
    }

    @PostMapping("/send-otp")
    public ResponseEntity<ApiRes> sendOtp(@Valid @RequestBody OtpReq otpReq) {

        String email = otpReq.getEmail();
        email = email.toLowerCase().trim();
        try {
            String otp = otpService.createOTP();
            otpService.cacheRedis(email, otp);
            emailService.SendOtpEmail(email, otp);

            return ResponseEntity.ok()
                    .body(ApiRes.success("otp send success"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiRes.error(e.getMessage()));
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<ApiRes> verifyOtp(@Valid @RequestBody OtpVerifyReq otpVerifyReq) {

        String email = otpVerifyReq.getEmail();
        String otp = otpVerifyReq.getOtp();
        email = email.toLowerCase().trim();

        logger.info("email: {}, otp: {}", email, otp);

        try {

            boolean verify = otpService.verifyOTP(email, otp);

            if (verify)
                return ResponseEntity.ok()
                        .body(ApiRes.success("otp verify success"));

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiRes.error("otp incorrect"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiRes.error(e.getMessage()));
        }
    }
}
