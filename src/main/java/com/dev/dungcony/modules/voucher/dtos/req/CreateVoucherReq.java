package com.dev.dungcony.modules.voucher.dtos.req;

import com.dev.dungcony.modules.voucher.enums.VoucherType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record CreateVoucherReq(
        @NotBlank String code,
        @NotNull VoucherType type,
        @Min(1) int value,
        @NotNull BigDecimal minOrderAmount,
        Instant startAt,
        Instant endAt,
        @NotEmpty List<UUID> userIds) {
}
