package com.dev.dungcony.modules.users.controllers;

import com.dev.dungcony.commons.dtos.AccountDetails;
import com.dev.dungcony.commons.dtos.ApiRes;
import com.dev.dungcony.modules.users.dtos.UserDto;
import com.dev.dungcony.modules.users.services.interfaces.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Users")
@RequiredArgsConstructor
@RestController
@RequestMapping("v1/api/user")
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<ApiRes<UserDto>> getMe(
            @AuthenticationPrincipal AccountDetails details) {
        return ResponseEntity.ok()
                .body(ApiRes.success("profile", userService.getUser(details.getId())));
    }

    @PostMapping("/create-user")
    public ResponseEntity<ApiRes<UserDto>> createUser(
            @AuthenticationPrincipal AccountDetails details,
            @Valid @RequestBody UserDto req) {
        return ResponseEntity.ok()
                .body(ApiRes.success("created", userService.createUser(details.getId(), req)));
    }

    @PutMapping("/update-profile")
    public ResponseEntity<ApiRes<UserDto>> updateProfile(
            @AuthenticationPrincipal AccountDetails details,
            @Valid @RequestBody UserDto req) {
        return ResponseEntity.ok()
                .body(ApiRes.success("updated", userService.updateUser(details.getId(), req)));
    }

}
