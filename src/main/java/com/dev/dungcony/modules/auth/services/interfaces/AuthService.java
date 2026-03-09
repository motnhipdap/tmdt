package com.dev.dungcony.modules.auth.services.interfaces;

import com.dev.dungcony.modules.auth.dtos.req.RegisReq;
import com.dev.dungcony.modules.auth.dtos.res.AccountRes;
import com.dev.dungcony.modules.auth.dtos.res.LoginRes;
import com.dev.dungcony.modules.auth.dtos.res.LoginResult;

public interface AuthService {
    AccountRes register(RegisReq req);

    LoginResult login(String username, String password, String deviceId);

    void logout(String refreshToken, String deviceId);

    LoginRes refreshToken(String refreshToken, Integer accId);
}
