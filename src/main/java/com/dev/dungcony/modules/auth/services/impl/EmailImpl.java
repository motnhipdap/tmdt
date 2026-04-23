package com.dev.dungcony.modules.auth.services.impl;

import com.dev.dungcony.modules.auth.config.MailProperties;
import com.dev.dungcony.modules.auth.exceptions.SendEmailException;
import com.dev.dungcony.modules.auth.services.interfaces.EmailService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class EmailImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final MailProperties mailProperties;

    @Value("${spring.mail.host:unknown}")
    private String mailHost;

    @Value("${spring.mail.port:587}")
    private int mailPort;

    @Value("${spring.mail.username:}")
    private String mailUsername;

    @Override
    public void sendNewPassword(String email, String newPassword) {
        send(email, "Mật khẩu mới của bạn", buildResetPassContent(newPassword));
    }

    @Override
    public void sendOtpChangeEmail(String email, String otp) {
        send(email, "Mã OTP xác nhận thay đổi email", buildEmailChangeContent(otp));
    }

    @Override
    public void sendOtpRegis(String email, String otp) {
        send(email, "Mã OTP đăng ký tài khoản", buildOtpContent(otp));
    }

    private void send(String email, String subject, String body) {
        try {
            log.info("Gửi email tới: {} | SMTP {}:{} | from: {} | user: {} (mật khẩu không ghi log)",
                    email, mailHost, mailPort, mailProperties.getFrom(), maskUsername(mailUsername));
            mailSender.send(getMail(
                    email,
                    subject,
                    body));
            log.info("Đã gửi email tới: {}", email);
        } catch (Exception e) {
            String chain = buildCauseChainForLog(e);
            String hint = diagnoseSmtpError(chain);
            log.error(
                    "Lỗi gửi email tới: {} | phân loại: {} | nội dung: {} | smtp: {}:{} | user: {}",
                    email, hint, chain, mailHost, mailPort, maskUsername(mailUsername), e);
            throw new SendEmailException();
        }
    }

    /** Che bớt username SMTP để log an toàn (vẫn nhận ra có set env hay chưa). */
    private static String maskUsername(String u) {
        if (u == null || u.isBlank()) {
            return "(rỗng — kiểm tra MAIL_USERNAME / spring.mail.username trên Railway)";
        }
        int at = u.indexOf('@');
        if (at <= 1) {
            return "***";
        }
        return u.charAt(0) + "***" + u.substring(at);
    }

    private static String buildCauseChainForLog(Throwable e) {
        StringBuilder sb = new StringBuilder();
        Throwable t = e;
        int d = 0;
        while (t != null && d < 10) {
            sb.append('[').append(t.getClass().getName()).append("] ");
            if (t.getMessage() != null) {
                sb.append(t.getMessage().replace('\n', ' ').trim());
            }
            sb.append(" | ");
            t = t.getCause();
            d++;
        }
        return sb.toString();
    }

    /**
     * Gợi ý nhanh từ log (Gmail: 535, 534, “Application-specific password”, bảo mật 2 bước, v.v.).
     */
    private static String diagnoseSmtpError(String allMessagesLower) {
        String s = allMessagesLower.toLowerCase();
        if (s.contains("authenticat")
                || s.contains("password")
                || s.contains("535")
                || s.contains("534")
                || s.contains("530 5.5.1")
                || s.contains("username and password not accepted")
                || s.contains("application-specific password")
                || s.contains("less secure app")) {
            return "SMTP_ĐĂNG_NHẬP_HOẶC_GMAIL_CHÍNH_SÁCH (mật khẩu ứng dụng / 2FA / tài khoản bị chặn bot)";
        }
        if (s.contains("could not connect")
                || s.contains("connection refused")
                || s.contains("connection timed out")
                || s.contains("unknown host")
                || s.contains("network is unreachable")
                || s.contains("i/o error")) {
            return "MẠNG_HOẶC_HOST_SAI (firewall, SMTP host/port, Railway chặn port 25?)";
        }
        if (s.contains("starttls")
                || s.contains("ssl")
                || s.contains("handshake")
                || s.contains("certificate")
                || s.contains("pkix")) {
            return "TLS_SSL (cấu hình starttls/ssl trên thiết bị hoặc proxy)";
        }
        return "KHÁC (xem nội dung exception phía dưới)";
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
