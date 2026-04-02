package com.dev.dungcony.modules.voucher.services.interfaces;

import com.dev.dungcony.modules.voucher.dtos.res.UserVoucherRes;
import com.dev.dungcony.modules.voucher.entities.UserVoucher;
import com.dev.dungcony.modules.voucher.entities.UserVoucherId;
import com.dev.dungcony.modules.voucher.enums.UserVoucherStatus;

import java.util.List;
import java.util.UUID;

public interface UserVoucherGetService {
    UserVoucher getUserVoucherById(UserVoucherId id);

    //trả về toàn bộ voucher của user
    List<UserVoucherRes> getUserVouchers(UUID userId);

    //trả về toàn bộ voucher của user
    List<UserVoucherRes> getUserVouchersByName(String name);

    //trả về toàn bộ của user với trạng thái cụ thể
    List<UserVoucherRes> getUserVouchersByStatus(UUID userId, UserVoucherStatus status);
}
