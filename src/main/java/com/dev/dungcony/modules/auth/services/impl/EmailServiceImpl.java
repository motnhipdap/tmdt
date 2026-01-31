package com.dev.dungcony.modules.auth.services.impl;

import com.dev.dungcony.modules.auth.exceptions.SendEmailException;
import com.dev.dungcony.modules.auth.services.interfaces.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

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
    public void send(String email, String subject, String body) {
        try {
            mailSender.send(getMail(
                    email,
                    subject,
                    body
            ));
            logger.info("Đã gửi email tới: {}", email);
        } catch (Exception e) {
            logger.error("Lỗi khi gửi email tới {}: {}", email, e.getMessage());
            throw new SendEmailException();
        }
    }

    @Override
    public String buildOtpContent(String otp) {
        return """
                Xin chào,
                
                Mã OTP của bạn là: %s
                
                Mã này sẽ hết hạn sau 5 phút.
                
                Nếu bạn không yêu cầu mã này, vui lòng bỏ qua email này.
                
                Trân trọng,
                DungCony Team
                """.formatted(otp);
    }

    @Override
    public String buildResetPassContent(String newPas) {
        return """
                Xin chào,
                
                Pass mới của bạn là: %s
                
                Hãy đổi mật khẩu khi nhận được tin nhắn này
                
                Trân trọng,
                DungCony Team
                """.formatted(newPas);
    }


    private SimpleMailMessage getMail(String email, String subject, String text) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setFrom(fromEmail);
        mail.setTo(email);
        mail.setSubject(subject);
        mail.setText(text);

        return mail;
    }
}
