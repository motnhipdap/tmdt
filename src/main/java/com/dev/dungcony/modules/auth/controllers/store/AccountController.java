package com.dev.dungcony.modules.auth.controllers.store;

import com.dev.dungcony.commons.dtos.AccountDetails;
import com.dev.dungcony.commons.dtos.ApiRes;
import com.dev.dungcony.modules.auth.dtos.req.UpdatePasswordReq;
import com.dev.dungcony.modules.auth.dtos.res.AccountRes;
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
