package com.dev.dungcony.modules.authorization.services.interfaces;

import com.dev.dungcony.modules.authorization.dtos.responses.AccountRes;
import com.dev.dungcony.modules.authorization.entities.Account;

public interface RefreshTokenService {
    String create(int id);

    AccountRes verify(String refreshToken);

    void revoke(String refreshToken);

    void revokeAll(Account account);
}
