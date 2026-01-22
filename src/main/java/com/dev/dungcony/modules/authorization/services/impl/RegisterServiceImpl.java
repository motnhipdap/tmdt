package com.dev.dungcony.modules.authorization.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dev.dungcony.modules.authorization.dtos.VerifyOtpRequest;
import com.dev.dungcony.modules.authorization.entities.Account;
import com.dev.dungcony.modules.authorization.repositories.AccRepository;
import com.dev.dungcony.modules.authorization.services.interfaces.RegisterService;

@Service
public class RegisterServiceImpl implements RegisterService {

    private static final Logger logger = LoggerFactory.getLogger(RegisterServiceImpl.class);

    private final AccRepository accountRepository;
    private final OtpServiceImpl otpService;
    private final EmailServiceImpl emailService;

    public RegisterServiceImpl(AccRepository accountRepository, OtpServiceImpl otpService,
            EmailServiceImpl emailService) {
        this.accountRepository = accountRepository;
        this.otpService = otpService;
        this.emailService = emailService;
    }

    /**
     * Gửi OTP về email
     */
    @Override
    @Transactional
    public void sendOtp(String email) {
        // Kiểm tra email đã tồn tại chưa
        if (accountRepository.existsByEmail(email)) {
            throw new RuntimeException("Email đã được đăng ký");
        }

        // Tạo và gửi OTP
        otpService.generateAndSendOtp(email);
        logger.info("Đã gửi OTP cho email: {}", email);
    }

    /**
     * Xác thực OTP và tạo tài khoản
     */
    @Override
    @Transactional
    public Account verifyOtpAndRegister(VerifyOtpRequest request) {
        // Xác thực OTP
        boolean isValidOtp = otpService.verifyOtp(request.getEmail(), request.getOtpCode());
        if (!isValidOtp) {
            throw new RuntimeException("OTP không hợp lệ hoặc đã hết hạn");
        }

        // Kiểm tra email đã tồn tại chưa (double check)
        if (accountRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email đã được đăng ký");
        }

        // Kiểm tra username đã tồn tại chưa
        if (accountRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Tên đăng nhập đã tồn tại");
        }

        // Kiểm tra phone đã tồn tại chưa
        if (accountRepository.existsByPhone(request.getPhone())) {
            throw new RuntimeException("Số điện thoại đã được đăng ký");
        }

        // Tạo tài khoản mới
        Account account = new Account();
        account.setEmail(request.getEmail());
        account.setUsername(request.getUsername());
        account.setPassword(request.getPassword()); // TODO: Cần hash password bằng BCrypt
        account.setPhone(request.getPhone());
        account.setRole("customer");
        account.setStatus("active");

        // Lưu vào database
        Account savedAccount = accountRepository.save(account);

        // Gửi email chào mừng
        emailService.sendWelcomeEmail(savedAccount.getEmail(), savedAccount.getUsername());

        logger.info("Đăng ký tài khoản thành công cho email: {}", request.getEmail());
        return savedAccount;
    }
}
