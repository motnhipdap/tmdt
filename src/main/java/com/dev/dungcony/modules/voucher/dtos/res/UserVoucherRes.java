package com.dev.dungcony.modules.voucher.dtos.res;

import com.dev.dungcony.modules.voucher.enums.UserVoucherStatus;
import com.dev.dungcony.modules.voucher.enums.VoucherStatus;
import com.dev.dungcony.modules.voucher.enums.VoucherType;

import java.math.BigDecimal;
import java.time.Instant;

public record UserVoucherRes(
        String code,
        VoucherType type,
        int value,
        BigDecimal minOrderAmount,
        VoucherStatus voucherStatus,
        UserVoucherStatus status,
        Instant startAt,
        Instant endAt,
        Instant createdAt,
        Instant usedAt) {
}
