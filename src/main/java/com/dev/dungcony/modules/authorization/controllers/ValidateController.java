package com.dev.dungcony.modules.authorization.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dev.dungcony.modules.authorization.dtos.responses.ApiRes;
import com.dev.dungcony.modules.authorization.services.interfaces.AccountService;

@RestController
@RequestMapping("v1/api/auth")
public class ValidateController {

    private final AccountService accountService;

    public ValidateController(AccountService accountService) {
        this.accountService = accountService;
    }

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
}
