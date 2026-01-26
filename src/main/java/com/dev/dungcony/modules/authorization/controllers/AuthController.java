package com.dev.dungcony.modules.authorization.controllers;

import com.dev.dungcony.commons.dtos.ApiRes;
import com.dev.dungcony.modules.authorization.dtos.AccountResult;
import com.dev.dungcony.modules.authorization.dtos.requests.LoginReq;
import com.dev.dungcony.modules.authorization.dtos.requests.RegisReq;
import com.dev.dungcony.modules.authorization.dtos.responses.LoginRes;
import com.dev.dungcony.modules.authorization.entities.Account;
import com.dev.dungcony.modules.authorization.enums.AccountEnum;
import com.dev.dungcony.modules.authorization.services.interfaces.AccountService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("v1/api/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AccountService accountService;

    public AuthController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiRes<LoginRes>> login(@Valid @RequestBody LoginReq loginReq) {
        String username = loginReq.username();
        String password = loginReq.password();

        logger.info("username: {}, password: {}", username, password);

        try {
            AccountResult<LoginRes> ans = accountService.authenticate(username, password);
            if (ans.aEnum() == AccountEnum.NOT_FOUND
                    || ans.aEnum() == AccountEnum.INCORRECT_PASSWORD) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiRes.error("Username or password incorrect"));
            }
            if (ans.aEnum() == AccountEnum.FAILED) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiRes.error("server error"));
            }

            return ResponseEntity.ok()
                    .body(ApiRes.success("account login success", ans.data()));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiRes.error(e.getMessage()));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<ApiRes<Void>> register(@Valid @RequestBody RegisReq req) {
        String email = req.email().toLowerCase();
        String password = req.password();
        String username = req.username();

        try {
            Account account = new Account();
            account.setEmail(email);
            account.setPassword(password);
            account.setUsername(username);
            account.setCreatedAt(LocalDateTime.now());

            AccountResult<Void> accResult = accountService.createAccount(account);

            if (accResult.aEnum() == AccountEnum.EMAIL_EXISTS) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiRes.error("Email đã tồn tại"));
            }
            if (accResult.aEnum() == AccountEnum.USERNAME_EXISTS) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiRes.error("Tên đăng nhập đã tồn tại"));
            }

            if (accResult.aEnum() == AccountEnum.FAILED) {
                logger.error("account created failed");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiRes.error("Có lỗi xảy ra"));
            }

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiRes.success("account created success"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiRes.error(e.getMessage()));
        }
    }

    // @PostMapping("/update-password")
    // public ResponseEntity<ApiRes> updateAccount(@Valid @RequestBody
    // UpdatePasswordReq req) {
    // String username = acc.getUsername();

    // try{
    // Account newAcc = new Account();

    // AccountEnum accEnum = accountService.updateAccount(newAcc);
    // }
    // }
}
