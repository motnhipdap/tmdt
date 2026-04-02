package com.dev.dungcony.modules.voucher.controllers.user;

import com.dev.dungcony.commons.dtos.AccountDetails;
import com.dev.dungcony.commons.dtos.ApiRes;
import com.dev.dungcony.modules.voucher.dtos.res.UserVoucherRes;
import com.dev.dungcony.modules.voucher.enums.UserVoucherStatus;
import com.dev.dungcony.modules.voucher.enums.VoucherStatus;
import com.dev.dungcony.modules.voucher.services.interfaces.UserVoucherCreateService;
import com.dev.dungcony.modules.voucher.services.interfaces.UserVoucherGetService;
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
public class UserController {

    private final UserVoucherGetService userVoucherGetService;

    @GetMapping
    public ResponseEntity<ApiRes<List<UserVoucherRes>>> getMyVouchers(
            @AuthenticationPrincipal AccountDetails account) {
        return ResponseEntity.ok(ApiRes.success(
                "User vouchers",
                userVoucherGetService.getUserVouchersByStatus(account.requireUserUuid(), UserVoucherStatus.AVAILABLE)));
    }
}
