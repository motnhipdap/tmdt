package com.dev.dungcony.modules.users.services.interfaces;

import com.dev.dungcony.modules.users.dtos.LoginReq;
import com.dev.dungcony.modules.users.dtos.LoginRes;

public interface UserServiceInterface {
    LoginRes login(LoginReq loginReq);
}
