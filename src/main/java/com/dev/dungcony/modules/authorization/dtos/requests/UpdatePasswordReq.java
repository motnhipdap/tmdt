package com.dev.dungcony.modules.authorization.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UpdatePasswordReq {
    private final String oldPass;
    private final String newPass;

}
