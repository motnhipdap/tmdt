package com.dev.dungcony.modules.voucher.services.interfaces;

import com.dev.dungcony.modules.voucher.dtos.res.UserVoucherRes;

import java.util.UUID;

public interface UserVoucherCreateService {
    void applyNewbieVoucher(UUID uid);

    UserVoucherRes apllyByCode(UUID uid, String code);
}
