package com.dev.dungcony.modules.voucher.controllers;

import com.dev.dungcony.commons.dtos.ApiRes;
import com.dev.dungcony.modules.voucher.dtos.req.AssignVoucherReq;
import com.dev.dungcony.modules.voucher.dtos.req.CreateVoucherReq;
import com.dev.dungcony.modules.voucher.services.interfaces.VoucherAdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/admin/vouchers")
public class AdminVoucherController {

    private final VoucherAdminService voucherAdminService;

    @PostMapping
    public ResponseEntity<ApiRes<String>> createVoucher(@Valid @RequestBody CreateVoucherReq req) {
        return ResponseEntity.ok(ApiRes.success("Voucher created", voucherAdminService.createVoucher(req)));
    }

    @PostMapping("/{voucherCode}/assign")
    public ResponseEntity<ApiRes<Void>> assignVoucher(
            @PathVariable String voucherCode,
            @Valid @RequestBody AssignVoucherReq req) {
        voucherAdminService.assignVoucher(voucherCode, req);
        return ResponseEntity.ok(ApiRes.success("Voucher assigned"));
    }
}
