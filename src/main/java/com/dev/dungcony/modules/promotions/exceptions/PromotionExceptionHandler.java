package com.dev.dungcony.modules.promotions.exceptions;

import com.dev.dungcony.commons.dtos.ApiRes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice(basePackages = "com.dev.dungcony.modules.promotions")
public class PromotionExceptionHandler {

    @ExceptionHandler(PromotionNotFoundException.class)
    public ResponseEntity<ApiRes<?>> handlePromotionNotFound(PromotionNotFoundException ex) {
        log.error("Promotion not found: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiRes.error(ex.getMessage()));
    }

    @ExceptionHandler(InvalidPromotionException.class)
    public ResponseEntity<ApiRes<?>> handleInvalidPromotion(InvalidPromotionException ex) {
        log.error("Invalid promotion: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiRes.error(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiRes<Map<String, String>>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        log.error("Validation errors: {}", errors);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiRes.error("Validation failed", errors));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiRes<?>> handleGenericException(Exception ex) {
        log.error("Unexpected error in promotion module: ", ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiRes.error("An unexpected error occurred"));
    }
}

