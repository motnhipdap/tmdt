package com.dev.dungcony.modules.authorization.services.impl;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dev.dungcony.modules.authorization.entities.OtpVerification;
import com.dev.dungcony.modules.authorization.repositories.OtpVerificationRepository;

@Service
public class OtpServiceImpl {

    private static final Logger logger = LoggerFactory.getLogger(OtpServiceImpl.class);
    private static final int OTP_LENGTH = 6;
    private static final int OTP_EXPIRY_MINUTES = 5;
    private static final SecureRandom random = new SecureRandom();

    private final OtpVerificationRepository otpRepository;
    private final EmailServiceImpl emailService;

    public OtpServiceImpl(OtpVerificationRepository otpRepository, EmailServiceImpl emailService) {
        this.otpRepository = otpRepository;
        this.emailService = emailService;
    }

    /**
     * Tạo và gửi OTP qua email
     */
    @Transactional
    public void generateAndSendOtp(String email) {
        // Xóa các OTP cũ chưa verify của email này
        otpRepository.deleteUnverifiedOtpsByEmail(email);

        // Tạo OTP mới
        String otpCode = generateOtpCode();
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(OTP_EXPIRY_MINUTES);

        // Lưu vào database
        OtpVerification otp = new OtpVerification(email, otpCode, expiresAt);
        otpRepository.save(otp);

        // Gửi email
        emailService.sendOtpEmail(email, otpCode);

        logger.info("Đã tạo và gửi OTP cho email: {}", email);
    }

    /**
     * Xác thực OTP
     */
    @Transactional
    public boolean verifyOtp(String email, String otpCode) {
        Optional<OtpVerification> otpOpt = otpRepository
                .findByEmailAndOtpCodeAndIsVerifiedFalseAndExpiresAtAfter(
                        email, otpCode, LocalDateTime.now());

        if (otpOpt.isPresent()) {
            OtpVerification otp = otpOpt.get();
            otp.setIsVerified(true);
            otpRepository.save(otp);
            logger.info("OTP xác thực thành công cho email: {}", email);
            return true;
        }

        logger.warn("OTP không hợp lệ hoặc đã hết hạn cho email: {}", email);
        return false;
    }

    /**
     * Tạo mã OTP ngẫu nhiên 6 số
     */
    private String generateOtpCode() {
        int otp = random.nextInt(900000) + 100000; // Số từ 100000 đến 999999
        return String.valueOf(otp);
    }

    /**
     * Tự động xóa các OTP đã hết hạn mỗi giờ
     */
    @Scheduled(cron = "0 0 * * * *") // Chạy mỗi giờ
    @Transactional
    public void cleanupExpiredOtps() {
        otpRepository.deleteExpiredOtps(LocalDateTime.now());
        logger.info("Đã xóa các OTP hết hạn");
    }
}
