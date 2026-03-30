package com.dev.dungcony.modules.voucher.services.impl;

import com.dev.dungcony.modules.users.repositories.UserRepository;
import com.dev.dungcony.modules.voucher.dtos.req.AssignVoucherReq;
import com.dev.dungcony.modules.voucher.dtos.req.CreateVoucherReq;
import com.dev.dungcony.modules.voucher.entities.UserVoucher;
import com.dev.dungcony.modules.voucher.entities.Voucher;
import com.dev.dungcony.modules.voucher.exceptions.VoucherInvalidException;
import com.dev.dungcony.modules.voucher.exceptions.VoucherNotFoundException;
import com.dev.dungcony.modules.voucher.repositories.UserVoucherRepository;
import com.dev.dungcony.modules.voucher.repositories.VoucherRepository;
import com.dev.dungcony.modules.voucher.services.interfaces.VoucherAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VoucherAdminServiceImpl implements VoucherAdminService {

    private final VoucherRepository voucherRepository;
    private final UserVoucherRepository userVoucherRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public String createVoucher(CreateVoucherReq req) {
        validateVoucherWindow(req.startAt(), req.endAt());

        Voucher voucher = new Voucher();
        voucher.setCode(req.code().trim().toUpperCase());
        voucher.setType(req.type());
        voucher.setValue(req.value());
        voucher.setMinOrderAmount(req.minOrderAmount());
        voucher.setStartAt(req.startAt());
        voucher.setEndAt(req.endAt());

        voucherRepository.save(voucher);
        assignUsers(voucher, req.userIds());
        return voucher.getCode();
    }

    @Override
    @Transactional
    public void assignVoucher(String voucherCode, AssignVoucherReq req) {
        Voucher voucher = voucherRepository.findByCode(voucherCode.trim().toUpperCase())
                .orElseThrow(VoucherNotFoundException::new);
        assignUsers(voucher, req.userIds());
    }

    private void assignUsers(Voucher voucher, List<UUID> userIds) {
        Set<UUID> uniqueUserIds = new HashSet<>(userIds);
        if (uniqueUserIds.isEmpty()) {
            throw new VoucherInvalidException("User list is required");
        }

        long existingUsers = userRepository.countByIdIn(uniqueUserIds);
        if (existingUsers != uniqueUserIds.size()) {
            throw new VoucherInvalidException("Some users do not exist");
        }

        for (UUID userId : uniqueUserIds) {
            if (userVoucherRepository.existsByUserIdAndVoucher_Id(userId, voucher.getId())) {
                continue;
            }

            UserVoucher userVoucher = new UserVoucher();
            userVoucher.setUserId(userId);
            userVoucher.setVoucher(voucher);
            try {
                userVoucherRepository.save(userVoucher);
            } catch (DataIntegrityViolationException ignored) {
                // Unique constraint protects against duplicate assignments in concurrent requests.
            }
        }
    }

    private void validateVoucherWindow(Instant startAt, Instant endAt) {
        if (startAt != null && endAt != null && !endAt.isAfter(startAt)) {
            throw new VoucherInvalidException("Voucher endAt must be after startAt");
        }
    }
}
