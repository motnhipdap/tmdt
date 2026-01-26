package com.dev.dungcony.modules.authorization.dtos.responses;

import com.dev.dungcony.modules.authorization.enums.AccountEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginResult {
    private final String token;
    private final AccountEnum accountEnum;

    public LoginResult(AccountEnum accountEnum) {
        this.token = "";
        this.accountEnum = accountEnum;
    }

    public LoginResult(String token) {
        this.token = token;
        this.accountEnum = AccountEnum.SUCCESS;
    }

}
