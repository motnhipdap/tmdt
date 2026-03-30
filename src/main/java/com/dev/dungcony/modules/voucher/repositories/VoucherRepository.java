package com.dev.dungcony.modules.voucher.repositories;

import com.dev.dungcony.modules.voucher.entities.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VoucherRepository extends JpaRepository<Voucher, Integer> {
    Optional<Voucher> findByCode(String code);
}
