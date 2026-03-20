package com.dev.dungcony.commons.controllers;

import com.dev.dungcony.commons.dtos.ApiErrorLogEntry;
import com.dev.dungcony.commons.dtos.ApiRes;
import com.dev.dungcony.commons.services.ApiErrorLogStore;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class LogController {

    private final ApiErrorLogStore apiErrorLogStore;

    @GetMapping("/log")
    public ResponseEntity<ApiRes<List<ApiErrorLogEntry>>> getApiErrorLogs() {
        return ResponseEntity.ok(ApiRes.success("api error logs", apiErrorLogStore.getAll()));
    }
}
