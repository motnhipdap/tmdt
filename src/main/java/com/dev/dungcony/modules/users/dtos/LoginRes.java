package com.dev.dungcony.modules.users.dtos;

public class LoginRes {

    private String token;
    private UserDTO user;

    public LoginRes(String token, UserDTO user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public UserDTO getUser() {
        return user;
    }
}
