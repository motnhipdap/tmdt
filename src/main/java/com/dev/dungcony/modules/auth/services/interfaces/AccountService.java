package com.dev.dungcony.modules.auth.services.interfaces;

import com.dev.dungcony.modules.auth.dtos.responses.AccountRes;

public interface AccountService {
    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    boolean updatePassword(int id, String oldPassword, String newPassword);

    AccountRes getProfileById(int id);

}
