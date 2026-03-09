package com.dev.dungcony.modules.auth.services.interfaces;

import com.dev.dungcony.modules.auth.enums.Status;

public interface AccountUpdateService {
    boolean updatePassword(int accId, String newPassword);

    void updateEmail(int accId, String newEmail);

    void verify(String email);

    void updateVerify(int accId, boolean isVerify);

    void updateStatus(int accId, Status newStatus);
}
