package com.dev.dungcony.modules.auth.controllers;

import com.dev.dungcony.commons.dtos.AccountDetails;
import com.dev.dungcony.commons.dtos.ApiRes;
import com.dev.dungcony.modules.auth.dtos.req.UpdatePasswordReq;
import com.dev.dungcony.modules.auth.dtos.res.AccountRes;
import com.dev.dungcony.modules.auth.services.interfaces.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("v1/api/account")
@Tag(name = "Account", description = "Quản lý tài khoản: xem profile, kiểm tra email/username, đổi mật khẩu")
public class AccountController {

    private final AccountService accountService;

    @Operation(summary = "Xem thông tin tài khoản hiện tại")
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiRes<AccountRes>> getMe(
            @AuthenticationPrincipal AccountDetails details) {
        return ResponseEntity.ok()
                .body(ApiRes.success("", accountService.getProfileById(details.getId())));
    }

    @Operation(summary = "Kiểm tra email đã tồn tại chưa")
    @GetMapping("/check_email")
    public ResponseEntity<ApiRes<Boolean>> check_email(@Valid @RequestParam String email) {
        return ResponseEntity.ok()
                .body(ApiRes.success("check email", accountService.existsByEmail(email)));
    }

    @Operation(summary = "Kiểm tra username đã tồn tại chưa")
    @GetMapping("/check_username")
    public ResponseEntity<ApiRes<Boolean>> check_username(@Valid @RequestParam String username) {
        return ResponseEntity.ok()
                .body(ApiRes.success("check username", accountService.existsByUsername(username)));
    }

    @Operation(summary = "Đổi mật khẩu")
    @PutMapping("/update_password")
    public ResponseEntity<ApiRes<Boolean>> updatePassword(
            @AuthenticationPrincipal AccountDetails details,
            @Valid @RequestBody UpdatePasswordReq req) {
        log.info(">>> updatePassword called, id={}", details.getId());
        boolean ok = accountService.updatePassword(details.getId(), req.oldPass(), req.newPass());
        return ResponseEntity.ok()
                .body(ApiRes.success("update_password res", ok));
    }

}
