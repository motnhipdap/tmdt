package com.dev.dungcony.modules.authorization.services.interfaces;

import com.dev.dungcony.modules.authorization.dtos.AccountResult;
import com.dev.dungcony.modules.authorization.dtos.requests.UpdatePasswordReq;
import com.dev.dungcony.modules.authorization.dtos.responses.AccountDetail;
import com.dev.dungcony.modules.authorization.dtos.responses.LoginRes;
import com.dev.dungcony.modules.authorization.entities.Account;

public interface AccountService {
    AccountResult<LoginRes> authenticate(String username, String password);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    AccountResult<Void> createAccount(Account acc);

    AccountResult<Void> updateAccount(Account acc);

    AccountResult<Void> updatePassword(String username, UpdatePasswordReq req);

    AccountResult<AccountDetail> getProfileById(int id);

}
