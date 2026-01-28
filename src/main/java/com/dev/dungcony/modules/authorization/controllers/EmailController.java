package com.dev.dungcony.modules.authorization.controllers;

import com.dev.dungcony.commons.dtos.ApiRes;
import com.dev.dungcony.modules.authorization.services.interfaces.EmailService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("v1/api/email")
public class EmailController {
    private static final Logger logger = LoggerFactory.getLogger(EmailController.class);

    private EmailService emailService;
    private PasswordEncoder passwordEncoder;

    @PutMapping("/confirm_change")
    @Transactional
    public ResponseEntity<ApiRes<Void>> comFirmChange(
            @Valid @RequestParam String token
    ) {

    }

}
