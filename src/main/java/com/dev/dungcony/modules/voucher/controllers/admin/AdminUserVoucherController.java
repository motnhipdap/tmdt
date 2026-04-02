package com.dev.dungcony.modules.voucher.controllers.admin;

import com.dev.dungcony.commons.dtos.ApiRes;
import com.dev.dungcony.modules.voucher.dtos.req.AssignVoucherReq;
import com.dev.dungcony.modules.voucher.dtos.req.CreateVoucherReq;
import com.dev.dungcony.modules.voucher.enums.VoucherStatus;
import com.dev.dungcony.modules.voucher.services.interfaces.UserVoucherGetService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/admin/user-vouchers")
public class AdminUserVoucherController {

    private final UserVoucherGetService userVoucherGetService;

    @Operation(summary = "Lấy voucher của user theo id", description = "Lấy voucher của user theo id")
    @GetMapping("/get-by-id")
    public ResponseEntity<ApiRes<?>> getUserVouchersById(
            @RequestParam(required = false) UUID uid) {
        return ResponseEntity.ok(ApiRes.success(
                "User vouchers",
                userVoucherGetService.getUserVouchers(uid)));
    }

    @Operation(summary = "Lấy voucher của user theo name", description = "Lấy voucher của user theo name")
    @GetMapping("/get-by-name")
    public ResponseEntity<ApiRes<?>> getUserVouchersByName(
            @RequestParam(required = false) String name) {
        return ResponseEntity.ok(ApiRes.success(
                "User vouchers",
                userVoucherGetService.getUserVouchersByName(name)));
    }
}
