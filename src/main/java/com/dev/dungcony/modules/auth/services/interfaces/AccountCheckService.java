package com.dev.dungcony.modules.auth.services.interfaces;

public interface AccountCheckService {
    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    void emailAndUsernameIsTrue(int accId, String email, String username);

    void checkPassword(int accId, String password);
}
