package com.dev.dungcony.modules.auth.controllers.store;


import com.dev.dungcony.commons.dtos.AccountDetails;
import com.dev.dungcony.commons.dtos.ApiRes;
import com.dev.dungcony.modules.auth.services.interfaces.AccountCheckService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("v1/api/account/check")
@Tag(name = "check", description = "Các API kiểm tra thông tin tài khoản như email, username, password")
public class CheckerController {
    private final AccountCheckService accountCheckService;

    @Operation(summary = "Kiểm tra email đã tồn tại chưa")
    @GetMapping("/exists-email")
    public ResponseEntity<ApiRes<Void>> checkEmail(@Valid @RequestParam String email) {
        accountCheckService.existsByEmail(email);
        return ResponseEntity.ok()
                .body(ApiRes.success("check email"));
    }

    @Operation(summary = "Kiểm tra username đã tồn tại chưa")
    @GetMapping("/exists-username")
    public ResponseEntity<ApiRes<Void>> checkUsername(@Valid @RequestParam String username) {
        accountCheckService.existsByUsername(username);
        return ResponseEntity.ok()
                .body(ApiRes.success("check username"));
    }

    @PostMapping("/password")
    public ResponseEntity<ApiRes<Void>> checkPass(
            @AuthenticationPrincipal AccountDetails detail,
            @RequestBody String password) {

        accountCheckService.checkPassword(detail.getId(), password);

        return ResponseEntity.ok()
                .body(ApiRes.success("password correct"));
    }
}
