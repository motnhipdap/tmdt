package com.dev.dungcony.modules.voucher.exceptions;

import com.dev.dungcony.commons.exceptions.AppException;
import org.springframework.http.HttpStatus;

public class VoucherInvalidException extends AppException {
    public VoucherInvalidException(String message) {
        super(HttpStatus.BAD_REQUEST, "VOUCHER_INVALID", message);
    }
}
