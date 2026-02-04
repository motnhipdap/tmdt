package com.dev.dungcony.commons.exceptions;

import com.dev.dungcony.commons.dtos.ApiRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiRes<Void>> handleAppException(AppException e) {
        return ResponseEntity
                .status(e.getStatus())
                .body(ApiRes.error(e.getMessage()));
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)  // Nếu validate fail thì trả về 400
    public String handleBindException(BindException e) {
        // Trả về message của lỗi đầu tiên
        String errorMessage = "Request không hợp lệ";
        if (e.getBindingResult().hasErrors())
            errorMessage = e.getBindingResult()
                    .getAllErrors()
                    .getFirst()
                    .getDefaultMessage();
        return errorMessage;
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiRes<Map<String, String>>> handleValidation(
            MethodArgumentNotValidException ex
    ) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors()
                .forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));
        log.error(ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiRes.error("Validation failed", errors));
    }


    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ApiRes<Void>> handleDatabaseError(DataAccessException ex) {
        log.error("Database error", ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiRes.error("Database error, please try again later"));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiRes<Void>> handleDuplicateKey() {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ApiRes.error("Có lỗi xảy ra"));
    }


    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<ApiRes<Void>> handleVersionException(Exception e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ApiRes.error("was updated by another user, please reload"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiRes<Void>> handleException(Exception e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiRes.error("Server Error"));
    }
}
