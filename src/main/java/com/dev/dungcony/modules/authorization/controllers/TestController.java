package com.dev.dungcony.modules.authorization.controllers;


import com.dev.dungcony.commons.dtos.ApiRes;
import com.dev.dungcony.modules.authorization.services.interfaces.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("v1/api/test")
public class TestController {

    private final RefreshTokenService refreshTokenService;

    @PostMapping("/revoke-all-redis")
    public ResponseEntity<ApiRes<Void>> revokeAllRedis(
            @RequestBody int uid
    ) {
        log.info("Revoke all redis" + uid);
        refreshTokenService.revokeAll(uid);
        return ResponseEntity.ok().build();
    }

}
