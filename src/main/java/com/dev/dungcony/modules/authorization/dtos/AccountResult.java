package com.dev.dungcony.modules.authorization.dtos;

import com.dev.dungcony.modules.authorization.enums.AccountEnum;

public record AccountResult<T>(
        AccountEnum aEnum,
        T data
) {
    public static <T> AccountResult<T> success(T data) {
        return new AccountResult<>(AccountEnum.SUCCESS, data);
    }

    public static <T> AccountResult<T> error(AccountEnum accountEnum) {
        return new AccountResult<>(accountEnum, null);
    }

    public static AccountResult<Void> success() {
        return new AccountResult<>(AccountEnum.SUCCESS, null);
    }
}
