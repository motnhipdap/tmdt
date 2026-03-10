package com.dev.dungcony.modules.auth.services.impl;

import com.dev.dungcony.modules.auth.config.MailProperties;
import com.dev.dungcony.modules.auth.exceptions.SendEmailException;
import com.dev.dungcony.modules.auth.services.interfaces.EmailService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class EmailImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final MailProperties mailProperties;

    @Override
    public void sendNewPassword(String email, String newPassword) {
        send(email, buildResetPassContent(newPassword), newPassword);
    }

    @Override
    public void sendOtpChangeEmail(String email, String otp) {
        send(email, buildEmailChangeContent(otp), otp);
    }

    @Override
    public void sendOtpRegis(String email, String otp) {
        send(email, buildOtpContent(otp), otp);
    }


    private void send(String email, String subject, String body) {
        try {
            mailSender.send(getMail(
                    email,
                    subject,
                    body));
            log.info("Đã gửi email tới: {}", email);
        } catch (Exception e) {
            log.error("Lỗi khi gửi email tới {}: {}", email, e.getMessage());
            throw new SendEmailException();
        }
    }

    private String buildOtpContent(String otp) {
        return """
                Xin chào,
                
                Mã OTP của bạn là: %s
                
                Mã này sẽ hết hạn sau 5 phút.
                
                Nếu bạn không yêu cầu mã này, vui lòng bỏ qua email này.
                
                Trân trọng,
                DungCony Team
                """.formatted(otp);
    }

    private String buildResetPassContent(String newPas) {
        return """
                Xin chào,
                
                Pass mới của bạn là: %s
                
                Hãy đổi mật khẩu khi nhận được tin nhắn này
                
                Trân trọng,
                DungCony Team
                """.formatted(newPas);
    }


    private String buildEmailChangeContent(String otp) {
        return """
                Xin chào,
                
                Nếu đây là yêu cầu của bạn, mã OTP để xác nhận thay đổi email là: %s
                
                Mã này sẽ hết hạn sau 5 phút.
                
                Nếu bạn không yêu cầu mã này, vui lòng bỏ qua email này.
                
                Trân trọng,
                DungCony Team
                """.formatted(otp);
    }


    private SimpleMailMessage getMail(String email, String subject, String text) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setFrom(mailProperties.getFrom());
        mail.setTo(email);
        mail.setSubject(subject);
        mail.setText(text);

        return mail;
    }

}
