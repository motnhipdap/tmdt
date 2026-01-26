package com.dev.dungcony.modules.authorization.dtos.responses;

import com.dev.dungcony.modules.authorization.enums.AccountEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountResult {
    private AccountEnum aEnum;
    private Object data;

    public AccountResult(AccountEnum e) {
        this.data = null;
        aEnum = e;
    }
}
