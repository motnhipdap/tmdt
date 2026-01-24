package com.dev.dungcony.modules.authorization.services.impl;

import com.dev.dungcony.modules.authorization.services.interfaces.EmailService;
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

    /**
     * Gửi email OTP
     */
    @Override
    public void SendOtpEmail(String reciever, String OTP) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(reciever);
            message.setSubject("Mã xác thực OTP - DungCony");
            message.setText(buildOtpEmailContent(OTP));

            mailSender.send(message);
            logger.info("Đã gửi OTP email tới: {}", reciever);
        } catch (Exception e) {
            logger.error("Lỗi khi gửi email tới {}: {}", reciever, e.getMessage());
            throw new RuntimeException("Không thể gửi email. Vui lòng thử lại sau.");
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

    /**
     * Gửi email chào mừng sau khi đăng ký thành công
     */
    public void sendWelcomeEmail(String toEmail, String username) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Chào mừng bạn đến với DungCony!");
            message.setText(buildWelcomeEmailContent(username));

            mailSender.send(message);
            logger.info("Đã gửi email chào mừng tới: {}", toEmail);
        } catch (Exception e) {
            logger.error("Lỗi khi gửi email chào mừng tới {}: {}", toEmail, e.getMessage());
            // Không throw exception vì đây không phải là critical error
        }
    }

    /**
     * Xây dựng nội dung email chào mừng
     */
    public String buildWelcomeEmailContent(String username) {
        return """
                Xin chào %s,
                
                Chào mừng bạn đã đăng ký tài khoản tại DungCony!
                
                Bạn đã đăng ký thành công và có thể bắt đầu sử dụng dịch vụ của chúng tôi.
                
                Nếu bạn có bất kỳ câu hỏi nào, đừng ngại liên hệ với chúng tôi.
                
                Trân trọng,
                DungCony Team
                """.formatted(username);
    }

}
