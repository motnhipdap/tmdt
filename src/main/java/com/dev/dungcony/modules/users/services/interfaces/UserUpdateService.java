package com.dev.dungcony.modules.users.services.interfaces;

import com.dev.dungcony.modules.users.dtos.res.UserRes;
import com.dev.dungcony.modules.users.dtos.req.UserUpdateReq;

public interface UserUpdateService {
    UserRes updateUser(int accId, UserUpdateReq req);
}
