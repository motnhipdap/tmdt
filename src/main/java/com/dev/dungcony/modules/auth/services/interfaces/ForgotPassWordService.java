package com.dev.dungcony.modules.auth.services.interfaces;

public interface ForgotPassWordService {
    void sendForgotPassWord(String email, String subject, String text);

    void verifyForgotPassWord(String email, String subject, String text);

}
