package com.dev.dungcony.modules.authorization.dtos.responses;

public record LoginResult(String token, String type, long expired, String refreshToken) {
}
