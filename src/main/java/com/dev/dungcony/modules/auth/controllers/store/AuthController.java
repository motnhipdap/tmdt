package com.dev.dungcony.modules.auth.controllers.store;

import com.dev.dungcony.commons.dtos.ApiRes;
import com.dev.dungcony.modules.auth.dtos.req.LoginReq;
import com.dev.dungcony.modules.auth.dtos.res.AcessTokenRes;
import com.dev.dungcony.modules.auth.dtos.res.LoginResult;
import com.dev.dungcony.modules.auth.services.interfaces.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("v1/api/public/auth")
@Tag(name = "Auth")
public class AuthController {

    private final AuthService authService;

    @Operation(
            summary = "Đăng nhập",
            description = """
                    Đăng nhập bằng username và password.
                    
                    **Trả về:**
                    - `token`: access token, đặt vào header `Authorization: Bearer <token>` cho các request tiếp theo
                    - `header`: tiền tố, luôn là `Bearer`
                    - `expiration`: thời gian hết hạn của access token (giây)
                    - Cookie `refresh_token` (httpOnly) được set tự động
                    
                    **Lưu ý:** Header `X-Device-Id` dùng để quản lý session theo thiết bị. FE tự sinh và lưu lại (ví dụ: UUID ngẫu nhiên lần đầu mở app).
                    """
    )
    @RequestBody(
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = LoginReq.class),
                    examples = @ExampleObject(
                            name = "Ví dụ",
                            value = """
                                    {
                                      "username": "dungcony",
                                      "password": "12345678"
                                    }
                                    """
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "Đăng nhập thành công",
                    headers = @Header(name = "Set-Cookie", description = "refresh_token=<token>; HttpOnly; Secure"),
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "login success",
                                      "data": {
                                        "token": "eyJhbGciOiJIUzUxMiJ9...",
                                        "header": "Bearer",
                                        "expiration": 3600
                                      }
                                    }
                                    """)
                    )
            ),
            @ApiResponse(responseCode = "401", description = "Sai username hoặc password",
                    content = @Content(examples = @ExampleObject(value = """
                            { "success": false, "message": "Invalid username or password", "data": null }
                            """))
            ),
            @ApiResponse(responseCode = "401", description = "Tài khoản chưa xác thực email",
                    content = @Content(examples = @ExampleObject(value = """
                            { "success": false, "message": "Account email is not verified", "data": null }
                            """))
            )
    })
    @PostMapping("/login")
    public ResponseEntity<ApiRes<AcessTokenRes>> login(
            @Valid @org.springframework.web.bind.annotation.RequestBody LoginReq loginReq,
            @Parameter(description = "ID định danh thiết bị, FE tự sinh UUID và lưu lại", required = true, example = "device-uuid-abc123")
            @RequestHeader("X-Device-Id") String deviceId) {
        log.info("Login req: {}", loginReq);
        LoginResult res = authService.login(loginReq.username(), loginReq.password(), deviceId);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, res.refreshToken())
                .body(ApiRes.success("login success", new AcessTokenRes(res.token(), res.expired())));
    }

    @Operation(
            summary = "Refresh token",
            description = """
                    Dùng `refresh_token` trong cookie để lấy access token mới khi access token hết hạn.
                    
                    **Yêu cầu:** Cookie `refresh_token` phải được gửi kèm (trình duyệt tự gửi nếu cùng domain).
                    
                    **Trả về:** access token mới.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy access token mới thành công",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "refresh_success",
                                      "data": {
                                        "token": "eyJhbGciOiJIUzUxMiJ9...",
                                        "header": "Bearer",
                                        "expiration": 3600
                                      }
                                    }
                                    """)
                    )
            ),
            @ApiResponse(responseCode = "401", description = "Refresh token không hợp lệ hoặc đã hết hạn",
                    content = @Content(examples = @ExampleObject(value = """
                            { "success": false, "message": "Token is not valid", "data": null }
                            """))
            )
    })
    @PostMapping("/refresh")
    public ResponseEntity<ApiRes<AcessTokenRes>> refresh(
            @Parameter(hidden = true) @CookieValue("refresh_token") String token) {
        return ResponseEntity.ok()
                .body(ApiRes.success("refresh_success", authService.refreshToken(token)));
    }

    @Operation(
            summary = "Đăng xuất",
            description = """
                    Xóa refresh token của thiết bị hiện tại khỏi Redis.
                    
                    **Yêu cầu:**
                    - Cookie `refresh_token`
                    - Header `X-Device-Id` (cùng giá trị lúc đăng nhập)
                    
                    Sau khi logout, access token cũ vẫn còn hiệu lực đến khi hết hạn (stateless JWT), nhưng refresh token bị thu hồi.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Đăng xuất thành công",
                    content = @Content(examples = @ExampleObject(value = """
                            { "success": true, "message": "logout success", "data": null }
                            """))
            )
    })
    @PostMapping("/logout")
    public ResponseEntity<ApiRes<Void>> logout(
            @Parameter(hidden = true) @CookieValue("refresh_token") String token,
            @Parameter(description = "ID thiết bị, phải khớp với lúc đăng nhập", required = true, example = "device-uuid-abc123")
            @RequestHeader("X-Device-Id") String deviceId) {
        log.info("Logout token: {}", token);
        authService.logout(token, deviceId);
        return ResponseEntity.ok().body(ApiRes.success("logout success"));
    }
}
