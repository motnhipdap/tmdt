package com.dev.dungcony.modules.authorization.services.interfaces;

import com.dev.dungcony.modules.authorization.dtos.LoginResult;
import com.dev.dungcony.modules.authorization.entities.Account;
import com.dev.dungcony.modules.authorization.enums.AccountEnum;

public interface AccountService {
    LoginResult authenticate(String username, String password);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    AccountEnum createAccount(Account acc);

    AccountEnum updateAccount(Account acc);

    Account getAccountByEmail(String email);

    Account getAccountByUsername(String username);
}
