package com.dev.dungcony.modules.users.controllers.user;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dev.dungcony.commons.dtos.AccountDetails;
import com.dev.dungcony.commons.dtos.ApiRes;
import com.dev.dungcony.modules.users.dtos.res.UserRes;
import com.dev.dungcony.modules.users.dtos.req.UserCreateReq;
import com.dev.dungcony.modules.users.services.interfaces.UserCreateService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Users")
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/api/user/create")
public class UserCreateController {

    private final UserCreateService userService;

    @Operation(summary = "Create new user")
    @PostMapping("/user")
    public ResponseEntity<ApiRes<UserRes>> createUser(
            @AuthenticationPrincipal AccountDetails details,
            @Valid @RequestBody UserCreateReq req) {
        return ResponseEntity.ok()
                .body(ApiRes.success("created", userService.createUser(details.getId(), req)));
    }
}
