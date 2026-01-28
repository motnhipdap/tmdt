package com.dev.dungcony.modules.authorization.helpers;

import java.security.SecureRandom;

public class Help {
    private static final SecureRandom random = new SecureRandom();

    public static String createOTP(int length) {
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < length; i++) {
            otp.append(random.nextInt(10));
        }
        return otp.toString();
    }
}
