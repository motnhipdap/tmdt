package com.dev.dungcony.modules.authorization.controllers;

import com.dev.dungcony.commons.dtos.ApiRes;
import com.dev.dungcony.modules.authorization.dtos.requests.UpdatePasswordReq;
import com.dev.dungcony.modules.authorization.dtos.responses.AccountRes;
import com.dev.dungcony.modules.authorization.helpers.AccountDetails;
import com.dev.dungcony.modules.authorization.services.interfaces.AccountService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/api/account")
public class AccountController {

    private final AccountService accountService;
    private final Logger log = LoggerFactory.getLogger(AccountController.class);

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiRes<AccountRes>> getMe(
            @AuthenticationPrincipal AccountDetails details) {
        return ResponseEntity.ok()
                .body(ApiRes.success("", accountService.getProfileById(details.getId())));
    }

    @GetMapping("/check_email")
    public ResponseEntity<ApiRes<Boolean>> check_email(@Valid @RequestParam String email) {
        return ResponseEntity.ok()
                .body(ApiRes.error("Email already exists!", accountService.existsByEmail(email)));
    }

    @GetMapping("/check_username")
    public ResponseEntity<ApiRes<Boolean>> check_username(@Valid @RequestParam String username) {
        return ResponseEntity.ok()
                .body(ApiRes.error("username already exists!", accountService.existsByUsername(username)));
    }

    @PutMapping("/update_password")
    public ResponseEntity<ApiRes<Boolean>> updatePassword(
            @AuthenticationPrincipal AccountDetails details,
            @Valid @RequestBody UpdatePasswordReq req) {
        log.info(">>> updatePassword called, id={}, old={}, new={}",
                details.getId(),
                req.oldPass(),
                req.newPass()
        );
        boolean ok = accountService.updatePassword(details.getId(), req.oldPass(), req.newPass());
        return ResponseEntity.ok()
                .body(ApiRes.success("update_password res", ok));
    }

}
