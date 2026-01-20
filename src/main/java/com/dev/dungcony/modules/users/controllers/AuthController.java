package com.dev.dungcony.modules.users.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dev.dungcony.modules.users.dtos.LoginReq;
import com.dev.dungcony.modules.users.dtos.LoginRes;
import com.dev.dungcony.modules.users.dtos.UserDTO;
import com.dev.dungcony.modules.users.services.impl.UserService;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginRes> login(@RequestBody LoginReq loginReq) {
        return ResponseEntity.ok(new LoginRes("token", new UserDTO(1L, "John Doe", "john.doe@example.com")));
    }

}
