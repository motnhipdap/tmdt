package com.dev.dungcony.modules.authorization.services.interfaces;

import com.dev.dungcony.modules.authorization.dtos.requests.RegisReq;
import com.dev.dungcony.modules.authorization.dtos.responses.LoginRes;
import com.dev.dungcony.modules.authorization.dtos.responses.LoginResult;

public interface AuthService {
    void register(RegisReq req);

    LoginResult login(String username, String password);

    LoginRes refreshToken(String refreshToken);
}
