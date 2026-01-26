package com.dev.dungcony.modules.authorization.services.interfaces;

import com.dev.dungcony.modules.authorization.dtos.requests.LoginReq;
import com.dev.dungcony.modules.authorization.dtos.requests.RegisReq;
import com.dev.dungcony.modules.authorization.dtos.responses.LoginRes;

public interface AuthService {
    void register(RegisReq regisReq);

    LoginRes login(LoginReq loginReq);
}
