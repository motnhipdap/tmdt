package com.dev.dungcony.modules.auth.services.interfaces;

import com.dev.dungcony.modules.auth.dtos.res.AccountRes;

public interface RefreshTokenService {
    String create(int accId, String deviceId);

    AccountRes verify(String refreshToken);

    void revoke(String refreshToken, String deviceId);

    void revokeAll(int accId);
}
