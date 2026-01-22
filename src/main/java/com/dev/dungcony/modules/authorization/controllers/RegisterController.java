package com.dev.dungcony.modules.authorization.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dev.dungcony.modules.authorization.dtos.ApiResponse;
import com.dev.dungcony.modules.authorization.dtos.SendOtpRequest;
import com.dev.dungcony.modules.authorization.dtos.VerifyOtpRequest;
import com.dev.dungcony.modules.authorization.entities.Account;
import com.dev.dungcony.modules.authorization.services.interfaces.RegisterService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class RegisterController {

    private static final Logger logger = LoggerFactory.getLogger(RegisterController.class);

    private final RegisterService registerService;

    public RegisterController(RegisterService registerService) {
        this.registerService = registerService;
    }

    /**
     * API gửi OTP về email
     * POST /api/v1/auth/send-otp
     */
    @PostMapping("/send-otp")
    public ResponseEntity<ApiResponse<Void>> sendOtp(
            @Valid @RequestBody SendOtpRequest request,
            BindingResult bindingResult) {

        // Validate request
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldError() != null
                    ? bindingResult.getFieldError().getDefaultMessage()
                    : "Dữ liệu không hợp lệ";
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(errorMessage));
        }

        try {
            registerService.sendOtp(request.getEmail());
            return ResponseEntity.ok(
                    ApiResponse.success("OTP đã được gửi về email " + request.getEmail()));
        } catch (Exception e) {
            logger.error("Lỗi khi gửi OTP: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * API xác thực OTP và đăng ký tài khoản
     * POST /api/v1/auth/register
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Account>> register(
            @Valid @RequestBody VerifyOtpRequest request,
            BindingResult bindingResult) {

        // Validate request
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldError() != null
                    ? bindingResult.getFieldError().getDefaultMessage()
                    : "Dữ liệu không hợp lệ";
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(errorMessage));
        }

        try {
            Account account = registerService.verifyOtpAndRegister(request);
            // Không trả về password trong response
            account.setPassword(null);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Đăng ký tài khoản thành công", account));
        } catch (Exception e) {
            logger.error("Lỗi khi đăng ký tài khoản: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
}
