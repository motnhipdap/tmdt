package com.dev.dungcony.modules.voucher.services.interfaces;

import com.dev.dungcony.modules.voucher.dtos.req.AssignVoucherReq;
import com.dev.dungcony.modules.voucher.dtos.req.CreateVoucherReq;

public interface VoucherAdminService {
    String createVoucher(CreateVoucherReq req);

    void assignVoucher(String voucherCode, AssignVoucherReq req);
}
