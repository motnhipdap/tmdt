package com.dev.dungcony.modules.auth.helpers;

public interface Generate {

    String otp(int length);

    String password(int length);
}
