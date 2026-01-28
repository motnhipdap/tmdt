package com.dev.dungcony.modules.authorization.services.interfaces;

import com.dev.dungcony.modules.authorization.dtos.responses.AccountRes;

public interface RefreshTokenService {
    String create(int userId, String deviceId);

    AccountRes verify(String refreshToken);

    void revoke(String refreshToken, String deviceId);

    void revokeAll(int userId);
}
