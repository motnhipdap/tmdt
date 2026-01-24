package com.dev.dungcony.modules.authorization.controllers;

import com.dev.dungcony.modules.authorization.dtos.LoginResult;
import com.dev.dungcony.modules.authorization.dtos.requests.LoginReq;
import com.dev.dungcony.modules.authorization.dtos.requests.OtpReq;
import com.dev.dungcony.modules.authorization.dtos.requests.OtpVerifyReq;
import com.dev.dungcony.modules.authorization.dtos.requests.RegisReq;
import com.dev.dungcony.modules.authorization.dtos.responses.ApiRes;
import com.dev.dungcony.modules.authorization.entities.Account;
import com.dev.dungcony.modules.authorization.enums.AccountEnum;
import com.dev.dungcony.modules.authorization.services.interfaces.AccountService;
import com.dev.dungcony.modules.authorization.services.interfaces.EmailService;
import com.dev.dungcony.modules.authorization.services.interfaces.OTPService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@RestController
@RequestMapping("v1/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final OTPService otpService;
    private final AccountService accountService;
    private final EmailService emailService;

    @GetMapping("/check-email")
    public ResponseEntity<ApiRes> checkEmail(@RequestParam("email") String email) {
        try {
            boolean exists = accountService.existsByEmail(email);
            if (exists)
                return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiRes.error("Email đã tồn tại"));

            return ResponseEntity.ok().body(ApiRes.success("Email hợp lệ"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiRes.error(e.getMessage()));
        }
    }

    @GetMapping("/check-username")
    public ResponseEntity<ApiRes> checkUsername(@RequestParam("username") String username) {
        try {
            boolean exists = accountService.existsByUsername(username);
            if (exists)
                return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiRes.error("username đã tồn tại"));

            return ResponseEntity.ok().body(ApiRes.success("username hợp lệ"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiRes.error(e.getMessage()));
        }
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

    @PostMapping("/register")
    public ResponseEntity<ApiRes> register(@Valid @RequestBody RegisReq req) {
        String email = req.getEmail().toLowerCase().trim();
        String password = req.getPassword().trim();
        String username = req.getUsername().trim();

        try {
            Account account = new Account();
            account.setEmail(email);
            account.setPassword(password);
            account.setUsername(username);
            account.setCreatedAt(LocalDateTime.now());

            AccountEnum accountEnum = accountService.createAccount(account);
            if (accountEnum == AccountEnum.EMAIL_EXISTS) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiRes.error("Email đã tồn tại"));
            }
            if (accountEnum == AccountEnum.USERNAME_EXISTS) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiRes.error("Tên đăng nhập đã tồn tại"));
            }

            if (accountEnum == AccountEnum.FAILED) {
                logger.error("account created failed");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiRes.error("Có lỗi xảy ra"));
            }

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiRes.success("account created success"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiRes.error(e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiRes> login(@Valid @RequestBody LoginReq loginReq) {
        String username = loginReq.getUsername();
        String password = loginReq.getPassword();

        logger.info("username: {}, password: {}", username, password);

        try {
            LoginResult ans = accountService.authenticate(username, password);
            if (ans.getAccountEnum() == AccountEnum.NOT_FOUND
                    || ans.getAccountEnum() == AccountEnum.INCORRECT_PASSWORD) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiRes.error("Username or password incorrect"));
            }
            if (ans.getAccountEnum() == AccountEnum.FAILED) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiRes.error("server error"));
            }

            return ResponseEntity.ok()
                    .body(ApiRes.success("account login success", ans));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiRes.error(e.getMessage()));
        }
    }
}
