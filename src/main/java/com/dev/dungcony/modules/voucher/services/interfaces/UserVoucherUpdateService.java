package com.dev.dungcony.modules.voucher.services.interfaces;

import java.time.Instant;

public interface UserVoucherUpdateService {
    int checkOrUpdate(Instant now);
}
