package com.dev.dungcony.modules.voucher.controllers;

import com.dev.dungcony.commons.dtos.AccountDetails;
import com.dev.dungcony.commons.dtos.ApiRes;
import com.dev.dungcony.modules.voucher.dtos.res.UserVoucherRes;
import com.dev.dungcony.modules.voucher.enums.UserVoucherStatus;
import com.dev.dungcony.modules.voucher.services.interfaces.UserVoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/user/vouchers")
public class UserVoucherController {

    private final UserVoucherService userVoucherService;

    @GetMapping
    public ResponseEntity<ApiRes<List<UserVoucherRes>>> getMyVouchers(
            @AuthenticationPrincipal AccountDetails account,
            @RequestParam(required = false) UserVoucherStatus status) {
        return ResponseEntity.ok(ApiRes.success(
                "User vouchers",
                userVoucherService.getUserVouchers(account.requireUserUuid(), status)));
    }
}
