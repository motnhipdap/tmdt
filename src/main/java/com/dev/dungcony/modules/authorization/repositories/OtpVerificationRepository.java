package com.dev.dungcony.modules.authorization.repositories;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dev.dungcony.modules.authorization.entities.OtpVerification;

@Repository
public interface OtpVerificationRepository extends JpaRepository<OtpVerification, Integer> {

    /**
     * Tìm OTP chưa verify và chưa hết hạn theo email và code
     */
    Optional<OtpVerification> findByEmailAndOtpCodeAndIsVerifiedFalseAndExpiresAtAfter(
            String email, String otpCode, LocalDateTime currentTime);

    /**
     * Tìm OTP mới nhất của email
     */
    Optional<OtpVerification> findFirstByEmailOrderByCreatedAtDesc(String email);

    /**
     * Xóa tất cả OTP đã hết hạn
     */
    @Modifying
    @Query("DELETE FROM OtpVerification o WHERE o.expiresAt < :currentTime")
    void deleteExpiredOtps(LocalDateTime currentTime);

    /**
     * Xóa tất cả OTP cũ của email (khi tạo OTP mới)
     */
    @Modifying
    @Query("DELETE FROM OtpVerification o WHERE o.email = :email AND o.isVerified = false")
    void deleteUnverifiedOtpsByEmail(String email);
}
