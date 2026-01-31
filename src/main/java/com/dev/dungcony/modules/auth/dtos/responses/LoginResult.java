package com.dev.dungcony.modules.auth.dtos.responses;

public record LoginResult(String token, String type, long expired, String refreshToken) {
}
