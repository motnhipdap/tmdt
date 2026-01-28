package com.dev.dungcony.modules.authorization.services.interfaces;

import com.dev.dungcony.modules.authorization.dtos.requests.UpdateEmailReq;
import com.dev.dungcony.modules.authorization.dtos.responses.AccountRes;

public interface AccountService {
    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    void updateEmail(int id, UpdateEmailReq req);

    boolean updatePassword(int id, String oldPassword, String newPassword);

    AccountRes getProfileById(int id);

}
