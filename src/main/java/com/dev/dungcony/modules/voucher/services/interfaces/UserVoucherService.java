package com.dev.dungcony.modules.voucher.services.interfaces;

import com.dev.dungcony.modules.voucher.dtos.VoucherApplyResult;
import com.dev.dungcony.modules.voucher.dtos.res.UserVoucherRes;
import com.dev.dungcony.modules.voucher.enums.UserVoucherStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface UserVoucherService {
    List<UserVoucherRes> getUserVouchers(UUID userId, UserVoucherStatus status);

    VoucherApplyResult previewApplyVoucher(UUID userId, String voucherCode, BigDecimal subtotalAmount);

    void markVoucherUsed(Integer userVoucherId, String orderCode);
}
