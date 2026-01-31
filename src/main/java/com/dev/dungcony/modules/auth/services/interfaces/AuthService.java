package com.dev.dungcony.modules.auth.services.interfaces;

import com.dev.dungcony.modules.auth.dtos.requests.RegisReq;
import com.dev.dungcony.modules.auth.dtos.responses.LoginRes;
import com.dev.dungcony.modules.auth.dtos.responses.LoginResult;

public interface AuthService {
    void register(RegisReq req);

    LoginResult login(String username, String password, String deviceId);

    void logout(String refreshToken, String deviceId);

    LoginRes refreshToken(String refreshToken);
}
