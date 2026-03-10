package com.dev.dungcony.modules.auth.controllers.store;

import com.dev.dungcony.commons.dtos.ApiRes;
import com.dev.dungcony.modules.auth.dtos.req.RegisReq;
import com.dev.dungcony.modules.auth.dtos.req.VerifyOtpReq;
import com.dev.dungcony.modules.auth.services.interfaces.AuthService;
import com.dev.dungcony.modules.auth.services.interfaces.VerifyOtpService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("v1/api/public/auth/regis")
@Tag(name = "2. Đăng ký", description = "Đăng ký tài khoản và xác thực OTP qua email")
public class RegisController {

    private final AuthService authService;
    private final VerifyOtpService verifyOtpService;

    @Operation(
            summary = "Bước 1 — Tạo tài khoản",
            description = """
                    Tạo tài khoản mới với email, username và password.
                    
                    **Flow đăng ký:**
                    1. Gọi API này → hệ thống tạo tài khoản (chưa kích hoạt) và gửi mã OTP 6 số về email
                    2. Gọi `POST /regis/verify` với mã OTP vừa nhận để kích hoạt tài khoản
                    
                    **Lưu ý:** Tài khoản chưa verify OTP sẽ không thể đăng nhập.
                    
                    **Ràng buộc:**
                    - `email`: đúng định dạng email, chưa tồn tại trong hệ thống
                    - `username`: 3–50 ký tự, chưa tồn tại trong hệ thống
                    - `password`: 8–50 ký tự
                    """
    )
    @RequestBody(
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RegisReq.class),
                    examples = @ExampleObject(value = """
                            {
                              "email": "user@example.com",
                              "username": "dungcony",
                              "password": "mypassword123"
                            }
                            """)
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tạo tài khoản thành công, OTP đã được gửi về email",
                    content = @Content(examples = @ExampleObject(value = """
                            { "success": true, "message": "register success", "data": null }
                            """))
            ),
            @ApiResponse(responseCode = "409", description = "Email hoặc username đã tồn tại",
                    content = @Content(examples = @ExampleObject(value = """
                            { "success": false, "message": "Email already exists", "data": null }
                            """))
            ),
            @ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ (sai định dạng email, username/password quá ngắn...)",
                    content = @Content(examples = @ExampleObject(value = """
                            { "success": false, "message": "Validation failed", "data": null }
                            """))
            )
    })
    @PostMapping("/")
    public ResponseEntity<ApiRes<Void>> register(@Valid @org.springframework.web.bind.annotation.RequestBody RegisReq req) {
        authService.register(req);
        return ResponseEntity.ok().body(ApiRes.success("register success"));
    }

    @Operation(
            summary = "Bước 2 — Xác thực OTP kích hoạt tài khoản",
            description = """
                    Nhập mã OTP 6 số đã được gửi về email để kích hoạt tài khoản.
                    
                    **Lưu ý:**
                    - OTP có hiệu lực trong **5 phút**
                    - Sau khi verify thành công, tài khoản được kích hoạt và có thể đăng nhập
                    - Nếu OTP hết hạn, cần đăng ký lại từ bước 1
                    """
    )
    @RequestBody(
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = VerifyOtpReq.class),
                    examples = @ExampleObject(value = """
                            {
                              "email": "user@example.com",
                              "otp": "123456"
                            }
                            """)
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Xác thực thành công, tài khoản đã được kích hoạt",
                    content = @Content(examples = @ExampleObject(value = """
                            { "success": true, "message": "register success", "data": null }
                            """))
            ),
            @ApiResponse(responseCode = "401", description = "OTP sai",
                    content = @Content(examples = @ExampleObject(value = """
                            { "success": false, "message": "OTP is invalid", "data": null }
                            """))
            ),
            @ApiResponse(responseCode = "401", description = "OTP đã hết hạn",
                    content = @Content(examples = @ExampleObject(value = """
                            { "success": false, "message": "OTP has expired", "data": null }
                            """))
            )
    })
    @PostMapping("/verify")
    public ResponseEntity<ApiRes<Void>> verifyOtp(@Valid @org.springframework.web.bind.annotation.RequestBody VerifyOtpReq req) {
        verifyOtpService.verifyOtpRegister(req);
        return ResponseEntity.ok().body(ApiRes.success("register success"));
    }
}
