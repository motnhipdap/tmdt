package com.dev.dungcony.modules.authorization.services.impl;

import com.dev.dungcony.modules.authorization.exceptions.SendEmailException;
import com.dev.dungcony.modules.authorization.services.interfaces.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    private final JavaMailSender mailSender;

    @Value("${spring.mail.from}")
    private String fromEmail;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendOtpEmail(String email, String OTP) {
        try {
            mailSender.send(getMail(
                    email,
                    "Mã xác thực OTP - DungCony",
                    buildOtpEmailContent(OTP)));
            logger.info("Đã gửi OTP email tới: {}", email);
        } catch (Exception e) {
            logger.error("Lỗi khi gửi email tới {}: {}", email, e.getMessage());
            throw new SendEmailException();
        }
    }

    @Override
    public void sendPasswordReset(String email) {
        try {
            mailSender.send(getMail(
                    email,
                    "New Password Reset",
                    buildRandomPassword()));
        } catch (MailException e) {
            throw new SendEmailException();
        }
    }

    private String buildOtpEmailContent(String otpCode) {
        return """
                Xin chào,

                Mã OTP của bạn là: %s

                Mã này sẽ hết hạn sau 5 phút.

                Nếu bạn không yêu cầu mã này, vui lòng bỏ qua email này.

                Trân trọng,
                DungCony Team
                """.formatted(otpCode);
    }

    @Override
    public String buildRandomPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$%!";
        int length = 12;

        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }

        return sb.toString();
    }

    SimpleMailMessage getMail(String email, String subject, String text) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setFrom(fromEmail);
        mail.setTo(email);
        mail.setSubject(subject);
        mail.setText(text);

        return mail;
    }
}
