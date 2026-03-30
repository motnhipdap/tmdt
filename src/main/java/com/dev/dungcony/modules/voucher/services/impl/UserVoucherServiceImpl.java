package com.dev.dungcony.modules.voucher.services.impl;

import com.dev.dungcony.modules.voucher.dtos.VoucherApplyResult;
import com.dev.dungcony.modules.voucher.dtos.res.UserVoucherRes;
import com.dev.dungcony.modules.voucher.entities.UserVoucher;
import com.dev.dungcony.modules.voucher.entities.Voucher;
import com.dev.dungcony.modules.voucher.enums.UserVoucherStatus;
import com.dev.dungcony.modules.voucher.enums.VoucherStatus;
import com.dev.dungcony.modules.voucher.enums.VoucherType;
import com.dev.dungcony.modules.voucher.exceptions.VoucherInvalidException;
import com.dev.dungcony.modules.voucher.repositories.UserVoucherRepository;
import com.dev.dungcony.modules.voucher.services.interfaces.UserVoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserVoucherServiceImpl implements UserVoucherService {

    private final UserVoucherRepository userVoucherRepository;

    @Override
    @Transactional(readOnly = true)
    public List<UserVoucherRes> getUserVouchers(UUID userId, UserVoucherStatus status) {
        List<UserVoucher> vouchers = status == null
                ? userVoucherRepository.findAllByUserIdOrderByCreatedAtDesc(userId)
                : userVoucherRepository.findAllByUserIdAndStatusOrderByCreatedAtDesc(userId, status);

        return vouchers.stream()
                .map(this::toRes)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public VoucherApplyResult previewApplyVoucher(UUID userId, String voucherCode, BigDecimal subtotalAmount) {
        if (voucherCode == null || voucherCode.isBlank()) {
            return VoucherApplyResult.noVoucher(subtotalAmount);
        }

        if (subtotalAmount == null || subtotalAmount.signum() < 0) {
            throw new VoucherInvalidException("Subtotal amount is invalid");
        }

        UserVoucher userVoucher = userVoucherRepository.findByUserIdAndVoucher_Code(
                        userId,
                        voucherCode.trim().toUpperCase())
                .orElseThrow(() -> new VoucherInvalidException("Voucher does not belong to user"));

        validateVoucher(userVoucher, subtotalAmount);

        BigDecimal discountAmount = calculateDiscount(subtotalAmount, userVoucher.getVoucher());
        BigDecimal finalAmount = subtotalAmount.subtract(discountAmount);

        return new VoucherApplyResult(
                userVoucher.getId(),
                userVoucher.getVoucher().getCode(),
                discountAmount,
                finalAmount);
    }

    @Override
    @Transactional
    public void markVoucherUsed(Integer userVoucherId, String orderCode) {
        if (userVoucherId == null) {
            return;
        }

        UserVoucher userVoucher = userVoucherRepository.findById(userVoucherId)
                .orElseThrow(() -> new VoucherInvalidException("Voucher assignment not found"));

        if (userVoucher.getStatus() != UserVoucherStatus.AVAILABLE) {
            throw new VoucherInvalidException("Voucher is no longer available");
        }

        userVoucher.setStatus(UserVoucherStatus.USED);
        userVoucher.setUsedAt(Instant.now());
        userVoucher.setUsedOrderCode(orderCode);
    }

    private void validateVoucher(UserVoucher userVoucher, BigDecimal subtotalAmount) {
        Voucher voucher = userVoucher.getVoucher();
        Instant now = Instant.now();

        if (userVoucher.getStatus() != UserVoucherStatus.AVAILABLE) {
            throw new VoucherInvalidException("Voucher has already been used");
        }

        if (voucher.getStatus() != VoucherStatus.ACTIVE) {
            throw new VoucherInvalidException("Voucher is not active");
        }

        if (voucher.getStartAt() != null && now.isBefore(voucher.getStartAt())) {
            throw new VoucherInvalidException("Voucher is not active yet");
        }

        if (voucher.getEndAt() != null && !now.isBefore(voucher.getEndAt())) {
            throw new VoucherInvalidException("Voucher has expired");
        }

        if (subtotalAmount.compareTo(voucher.getMinOrderAmount()) < 0) {
            throw new VoucherInvalidException("Order does not meet voucher minimum amount");
        }
    }

    private BigDecimal calculateDiscount(BigDecimal subtotalAmount, Voucher voucher) {
        if (voucher.getType() == VoucherType.FIXED) {
            return BigDecimal.valueOf(voucher.getValue()).min(subtotalAmount);
        }

        return subtotalAmount
                .multiply(BigDecimal.valueOf(voucher.getValue()))
                .divide(BigDecimal.valueOf(100), 0, RoundingMode.HALF_UP)
                .min(subtotalAmount);
    }

    private UserVoucherRes toRes(UserVoucher userVoucher) {
        Voucher voucher = userVoucher.getVoucher();
        return new UserVoucherRes(
                voucher.getCode(),
                voucher.getType(),
                voucher.getValue(),
                voucher.getMinOrderAmount(),
                voucher.getStatus(),
                userVoucher.getStatus(),
                voucher.getStartAt(),
                voucher.getEndAt(),
                userVoucher.getCreatedAt(),
                userVoucher.getUsedAt());
    }
}
