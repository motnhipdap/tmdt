package com.dev.dungcony.modules.authorization.controllers;

import com.dev.dungcony.modules.authorization.dtos.responses.AccountDetail;
import com.dev.dungcony.commons.dtos.ApiRes;
import com.dev.dungcony.modules.authorization.enums.AccountEnum;
import com.dev.dungcony.modules.authorization.services.interfaces.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/api/account")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiRes<AccountDetail>> getMe(Authentication authentication) {
        String username = authentication.getName();
        Integer userId = (Integer) authentication.getDetails();
        if (userId == null)
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiRes.error("not logged in"));

        var ans = accountService.getProfileById(userId);

        if (ans == null)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiRes.error("server error"))
        if (ans.aEnum() == AccountEnum.NOT_FOUND)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiRes.error("not found"));
        if (ans.aEnum() == AccountEnum.FAILED)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiRes.error("server error"))

        return ResponseEntity.ok().body(ApiRes.success("Account retrieved", ans.data()));
    }

}
