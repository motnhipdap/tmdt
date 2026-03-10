package com.dev.dungcony.modules.auth.services.interfaces;

import com.dev.dungcony.modules.auth.dtos.res.AccountRes;

public interface AccountCreateService {
    void createAccount(String email, String username, String password);
}
