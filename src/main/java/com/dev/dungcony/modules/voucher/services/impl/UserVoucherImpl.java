package com.dev.dungcony.modules.voucher.services.impl;

import com.dev.dungcony.modules.users.dtos.res.UserRes;
import com.dev.dungcony.modules.users.services.interfaces.UserGetService;
import com.dev.dungcony.modules.voucher.dtos.res.UserVoucherRes;
import com.dev.dungcony.modules.voucher.entities.UserVoucher;
import com.dev.dungcony.modules.voucher.entities.UserVoucherId;
import com.dev.dungcony.modules.voucher.entities.Voucher;
import com.dev.dungcony.modules.voucher.enums.VoucherType;
import com.dev.dungcony.modules.voucher.enums.UserVoucherStatus;
import com.dev.dungcony.modules.voucher.enums.VoucherStatus;
import com.dev.dungcony.modules.voucher.exceptions.UserVoucherNotAvailable;
import com.dev.dungcony.modules.voucher.mappers.UserVoucherMapper;
import com.dev.dungcony.modules.voucher.repositories.UserVoucherRepository;
import com.dev.dungcony.modules.voucher.services.interfaces.UserVoucherCreateService;
import com.dev.dungcony.modules.voucher.services.interfaces.UserVoucherGetService;
import com.dev.dungcony.modules.voucher.services.interfaces.UserVoucherUpdateService;
import com.dev.dungcony.modules.voucher.services.interfaces.VoucherGetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserVoucherImpl implements UserVoucherCreateService, UserVoucherGetService, UserVoucherUpdateService {

    private final UserVoucherRepository userVoucherRepository;
    private final VoucherGetService voucherGetService;
    private final UserGetService userGetService;

    //---------------------------CREATE USER VOUCHER-----------------------------//

    @Override
    public void applyNewbieVoucher(UUID uid) {
        List<Voucher> vouchers = voucherGetService.getByTypeAndStatus(VoucherType.NEWBIE, VoucherStatus.ACTIVE);

        List<UserVoucher> uvs = new ArrayList<>();

        for (Voucher v : vouchers) {
            UserVoucher uv = new UserVoucher();
            uv.setId(new UserVoucherId(v.getId(), uid));
            uv.setStatus(UserVoucherStatus.AVAILABLE);
            uv.setVoucher(v);
            uvs.add(uv);
        }

        userVoucherRepository.saveAll(uvs);
    }

    @Override
    public UserVoucherRes apllyByCode(UUID uid, String code) {

        Voucher voucher = voucherGetService.getVoucherByCode(code);

        if (voucher.getVoucherType() != VoucherType.GLOBAL)
            throw new UserVoucherNotAvailable();

        UserVoucherId id = new UserVoucherId(voucher.getId(), uid);
        UserVoucher uv = getUserVoucherById(id);

        if (uv == null) {
            uv = new UserVoucher();
            uv.setId(id);
            uv.setStatus(UserVoucherStatus.AVAILABLE);
            uv.setVoucher(voucher);
        } else {
            if (uv.getStatus() == UserVoucherStatus.EXPIRED || uv.getStatus() == UserVoucherStatus.USED)
                throw new UserVoucherNotAvailable("User has already used this voucher and it is expired");
        }
        userVoucherRepository.save(uv);

        return UserVoucherMapper.toRes(uv);
    }

    //---------------------------GET USER VOUCHER-----------------------------//


    @Override
    public UserVoucher getUserVoucherById(UserVoucherId id) {
        UserVoucher uv = userVoucherRepository.findById(id).orElse(null);

        return uv;
    }

    @Override
    public List<UserVoucherRes> getUserVouchers(UUID userId) {

        List<UserVoucher> uvs = userVoucherRepository.findAllByUserId(userId);

        return uvs.stream()
                .map(UserVoucherMapper::toRes)
                .toList();
    }

    @Override
    public List<UserVoucherRes> getUserVouchersByName(String name) {
        UserRes user = userGetService.getByName(name);

        List<UserVoucher> uvs = userVoucherRepository.findAllByUserId(user.id());

        return uvs.stream()
                .map(UserVoucherMapper::toRes)
                .toList();
    }

    @Override
    public List<UserVoucherRes> getUserVouchersByStatus(UUID userId, UserVoucherStatus status) {

        List<UserVoucher> uvs = userVoucherRepository.findAllByUserIdByStatus(userId, status);

        return uvs.stream()
                .map(UserVoucherMapper::toRes)
                .toList();
    }

    //---------------------------UPDATE USER VOUCHER -----------------------//

    @Transactional
    @Override
    public int checkOrUpdate(Instant now) {

        List<UserVoucherStatus> notChecks = List.of(UserVoucherStatus.EXPIRED, UserVoucherStatus.USED);

        return userVoucherRepository.checkOrUpdate(
                notChecks,
                now
        );
    }
}
