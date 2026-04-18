package com.dev.dungcony.commons.exceptions;

import com.dev.dungcony.commons.dtos.ApiRes;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiRes<Void>> handleAppException(AppException e, HttpServletRequest request) {
        captureApiError(request, e.getStatus(), e, e.getMessage());
        return ResponseEntity
                .status(e.getStatus())
                .body(ApiRes.error(e.getMessage()));
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) // Nếu validate fail thì trả về 400
    public String handleBindException(BindException e, HttpServletRequest request) {
        // Trả về message của lỗi đầu tiên
        String errorMessage = "Request không hợp lệ";
        if (e.getBindingResult().hasErrors())
            errorMessage = e.getBindingResult()
                    .getAllErrors()
                    .getFirst()
                    .getDefaultMessage();
        captureApiError(request, HttpStatus.BAD_REQUEST, e, errorMessage);
        return errorMessage;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiRes<Map<String, String>>> handleValidation(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors()
                .forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));
        log.error(ex.getMessage(), ex);
        captureApiError(request, HttpStatus.BAD_REQUEST, ex, "Validation failed");
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiRes.error("Validation failed", errors));
    }

    @ExceptionHandler(MissingRequestCookieException.class)
    public ResponseEntity<ApiRes<Void>> handleMissingCookie(
            MissingRequestCookieException ex,
            HttpServletRequest request) {
        captureApiError(request, HttpStatus.UNAUTHORIZED, ex, "Missing refresh_token cookie");
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ApiRes.error("Missing refresh_token cookie"));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiRes<Void>> handleIllegalArgument(
            IllegalArgumentException ex, HttpServletRequest request) {
        captureApiError(request, HttpStatus.CONFLICT, ex, ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ApiRes.error(ex.getMessage()));
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ApiRes<Void>> handleDatabaseError(DataAccessException ex, HttpServletRequest request) {
        if (isDuplicateKeyError(ex)) {
            captureApiError(request, HttpStatus.CONFLICT, ex, "Email hoặc username đã tồn tại");
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(ApiRes.error("Email hoặc username đã tồn tại"));
        }

        log.error("Database error", ex);
        captureApiError(request, HttpStatus.INTERNAL_SERVER_ERROR, ex, "Database error, please try again later");
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiRes.error("Database error, please try again later"));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiRes<Void>> handleDuplicateKey(DataIntegrityViolationException ex,
                                                           HttpServletRequest request) {
        log.warn("Data integrity violation", ex);
        captureApiError(request, HttpStatus.CONFLICT, ex, "Email hoặc username đã tồn tại");

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ApiRes.error("Email hoặc username đã tồn tại"));
    }

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<ApiRes<Void>> handleVersionException(
            ObjectOptimisticLockingFailureException ex,
            HttpServletRequest request) {
        captureApiError(request, HttpStatus.CONFLICT, ex, "was updated by another user, please reload");
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ApiRes.error("was updated by another user, please reload"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiRes<Void>> handleException(Exception ex, HttpServletRequest request) {
        log.error("Unhandled exception", ex);
        captureApiError(request, HttpStatus.INTERNAL_SERVER_ERROR, ex, "Server Error");
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiRes.error("Server Error"));
    }

    private boolean isDuplicateKeyError(DataAccessException ex) {
        if (ex instanceof DataIntegrityViolationException) {
            return true;
        }

        String message = ex.getMostSpecificCause() != null
                ? ex.getMostSpecificCause().getMessage()
                : ex.getMessage();

        if (message == null) {
            return false;
        }

        String normalized = message.toLowerCase(Locale.ROOT);
        return normalized.contains("duplicate key")
                || normalized.contains("unique constraint")
                || normalized.contains("sqlstate: 23505")
                || normalized.contains("tbl_accounts_username_key")
                || normalized.contains("tbl_accounts_email_key");
    }

    private void captureApiError(HttpServletRequest request, HttpStatus status, Exception ex, String clientMessage) {
        if (request == null) {
            return;
        }

        String path = request.getRequestURI();
        if (path == null || !path.startsWith("/v1/api/")) {
            return;
        }
    }
}
