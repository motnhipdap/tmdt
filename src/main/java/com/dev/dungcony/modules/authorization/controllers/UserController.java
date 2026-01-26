package com.dev.dungcony.modules.authorization.controllers;

import com.dev.dungcony.commons.dtos.ApiRes;
import com.dev.dungcony.modules.authorization.dtos.AccountResult;
import com.dev.dungcony.modules.authorization.dtos.requests.ChangePasswordReq;
import com.dev.dungcony.modules.authorization.enums.AccountEnum;
import com.dev.dungcony.modules.authorization.services.interfaces.AccountService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * Controller cho các endpoint cần JWT authentication
 * Tất cả endpoints trong đây yêu cầu user phải login và gửi kèm JWT token
 */
@RestController
@RequestMapping("/v1/api/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final AccountService accountService;

    public UserController(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * Test endpoint để kiểm tra JWT authentication
     */
    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiRes> getProfile(Authentication authentication) {
        String username = authentication.getName();
        Integer userId = (Integer) authentication.getDetails();

        return ResponseEntity.ok()
                .body(ApiRes.success("Profile retrieved",
                        new ProfileInfo(userId, username)));
    }

    /**
     * Đổi mật khẩu (yêu cầu JWT authentication)
     * Username sẽ tự động lấy từ JWT token
     */
    @PostMapping("/change-password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiRes> changePassword(
            @Valid @RequestBody ChangePasswordReq req,
            Authentication authentication) {

        String username = authentication.getName();
        logger.info("User {} is changing password", username);

        // Validate: new password != old password
        if (req.getOldPassword().equals(req.getNewPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiRes.error("Mật khẩu mới phải khác mật khẩu cũ"));
        }

        // Validate: new password == confirm password
        if (!req.getNewPassword().equals(req.getConfirmPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiRes.error("Mật khẩu mới và xác nhận mật khẩu không khớp"));
        }

        try {
            // Tạo UpdatePasswordReq để gọi service
            UpdatePasswordReq updateReq = new UpdatePasswordReq(
                    req.getOldPassword(),
                    req.getNewPassword());

            AccountResult result = accountService.updatePassword(username, updateReq);

            if (result.getAEnum() == AccountEnum.NOT_FOUND) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiRes.error("Tài khoản không tồn tại"));
            }

            if (result.getAEnum() == AccountEnum.INCORRECT_PASSWORD) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiRes.error("Mật khẩu cũ không chính xác"));
            }

            if (result.getAEnum() == AccountEnum.FAILED) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(ApiRes.error("Có lỗi xảy ra khi đổi mật khẩu"));
            }

            logger.info("User {} changed password successfully", username);
            return ResponseEntity.ok()
                    .body(ApiRes.success("Đổi mật khẩu thành công. Vui lòng đăng nhập lại."));

        } catch (Exception e) {
            logger.error("Error changing password for user {}: {}", username, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiRes.error("Có lỗi xảy ra: " + e.getMessage()));
        }
    }

    /**
     * Inner record cho profile response
     */
    private record ProfileInfo(Integer userId, String username) {
    }
}
