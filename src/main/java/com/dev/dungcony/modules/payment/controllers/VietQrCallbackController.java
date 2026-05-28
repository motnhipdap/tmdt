package com.dev.dungcony.modules.payment.controllers;

import com.dev.dungcony.modules.payment.dtos.req.VietQrTransactionSyncReq;
import com.dev.dungcony.modules.payment.dtos.res.VietQrTokenErrorRes;
import com.dev.dungcony.modules.payment.dtos.res.VietQrTransactionSyncRes;
import com.dev.dungcony.modules.payment.services.interfaces.VietQrCallbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "VietQR Callback")
public class VietQrCallbackController {

    private static final String VIETQR_AUTH_HEADER = "X-VietQR-Authorization";

    private final VietQrCallbackService vietQrCallbackService;

    @Operation(summary = "VietQR get token", description = "Endpoint cấp Bearer token cho VietQR callback")
    @SecurityRequirements
    @PostMapping("/vqr/api/token_generate")
    public ResponseEntity<?> generateToken(
            @Parameter(hidden = true)
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @Parameter(description = "Swagger test only. Use Basic base64(username:password), for example: Basic ZHVuZ2Nvbnk6ZHVuZ2Nvbnk=")
            @RequestHeader(value = VIETQR_AUTH_HEADER, required = false) String vietQrAuthorization) {

        return vietQrCallbackService.createToken(resolveVietQrAuthorization(vietQrAuthorization, authorization))
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(new VietQrTokenErrorRes("FAILED", "INVALID_CREDENTIALS")));
    }

    @Operation(summary = "VietQR transaction sync", description = "Endpoint nhận biến động số dư từ VietQR")
    @SecurityRequirements
    @PostMapping({
            "/vqr/bank/api/transaction-sync",
            "/vqr/bank/api/test/transaction-callback"
    })
    public ResponseEntity<VietQrTransactionSyncRes> syncTransaction(
            @Parameter(hidden = true)
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @Parameter(description = "Swagger test only. Use Bearer access_token from /vqr/api/token_generate. Keep the normal user bearerAuth unchanged.")
            @RequestHeader(value = VIETQR_AUTH_HEADER, required = false) String vietQrAuthorization,
            @RequestBody VietQrTransactionSyncReq req) {

        if (!vietQrCallbackService.validateBearerToken(resolveVietQrAuthorization(vietQrAuthorization, authorization))) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(VietQrTransactionSyncRes.failure(
                            "INVALID_TOKEN",
                            "Invalid, expired, or non-VietQR callback token"));
        }

        try {
            VietQrTransactionSyncRes res = vietQrCallbackService.syncTransaction(req);
            return ResponseEntity.ok(res);
        } catch (RuntimeException ex) {
            log.warn("VietQR transaction sync failed: {}", ex.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(VietQrTransactionSyncRes.failure(
                            "TRANSACTION_FAILED",
                            ex.getMessage()));
        }
    }

    private String resolveVietQrAuthorization(String vietQrAuthorization, String authorization) {
        if (vietQrAuthorization != null && !vietQrAuthorization.isBlank()) {
            return vietQrAuthorization;
        }
        return authorization;
    }
}
