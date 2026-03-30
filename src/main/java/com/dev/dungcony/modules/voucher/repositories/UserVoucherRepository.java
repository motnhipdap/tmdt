package com.dev.dungcony.modules.voucher.repositories;

import com.dev.dungcony.modules.voucher.entities.UserVoucher;
import com.dev.dungcony.modules.voucher.enums.UserVoucherStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserVoucherRepository extends JpaRepository<UserVoucher, Integer> {
    Optional<UserVoucher> findByUserIdAndVoucher_Code(UUID userId, String voucherCode);

    boolean existsByUserIdAndVoucher_Id(UUID userId, Integer voucherId);

    List<UserVoucher> findAllByUserIdOrderByCreatedAtDesc(UUID userId);

    List<UserVoucher> findAllByUserIdAndStatusOrderByCreatedAtDesc(UUID userId, UserVoucherStatus status);
}
