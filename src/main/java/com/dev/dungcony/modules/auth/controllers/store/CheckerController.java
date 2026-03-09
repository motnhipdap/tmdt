package com.dev.dungcony.modules.auth.controllers.store;


import com.dev.dungcony.commons.dtos.AccountDetails;
import com.dev.dungcony.commons.dtos.ApiRes;
import com.dev.dungcony.modules.auth.services.interfaces.AccountCheckValidService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("v1/api/auth/check")
public class CheckerController {
    private final AccountCheckValidService accountCheckValidService;

    @Operation(summary = "Kiểm tra email đã tồn tại chưa")
    @GetMapping("/exists-email")
    public ResponseEntity<ApiRes<Boolean>> checkEmail(@Valid @RequestParam String email) {
        return ResponseEntity.ok()
                .body(ApiRes.success("check email", accountCheckValidService.existsByEmail(email)));
    }

    @Operation(summary = "Kiểm tra username đã tồn tại chưa")
    @GetMapping("/exists-username")
    public ResponseEntity<ApiRes<Boolean>> checkUsername(@Valid @RequestParam String username) {
        return ResponseEntity.ok()
                .body(ApiRes.success("check username", accountCheckValidService.existsByUsername(username)));
    }

    @PostMapping("/password")
    public ResponseEntity<ApiRes<Void>> checkPass(
            @AuthenticationPrincipal AccountDetails detail,
            @RequestBody String password) {

        accountCheckValidService.checkPassword(detail.getId(), password);

        return ResponseEntity.ok()
                .body(ApiRes.success("password correct"));
    }

}
