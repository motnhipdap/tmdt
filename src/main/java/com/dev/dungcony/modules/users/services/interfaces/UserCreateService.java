package com.dev.dungcony.modules.users.services.interfaces;

import com.dev.dungcony.modules.users.dtos.UserRes;
import com.dev.dungcony.modules.users.dtos.req.UserCreateReq;

public interface UserCreateService {
    UserRes createUser(int accId, UserCreateReq req);

}
