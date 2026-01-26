package com.dev.dungcony.modules.authorization.services.interfaces;

import com.dev.dungcony.modules.authorization.dtos.requests.UpdatePasswordReq;
import com.dev.dungcony.modules.authorization.dtos.responses.AccountResult;
import com.dev.dungcony.modules.authorization.dtos.responses.LoginResult;
import com.dev.dungcony.modules.authorization.entities.Account;

public interface AccountService {
    LoginResult authenticate(String username, String password);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    AccountResult createAccount(Account acc);

    AccountResult updateAccount(Account acc);

    AccountResult updatePassword(String username, UpdatePasswordReq req);
}
