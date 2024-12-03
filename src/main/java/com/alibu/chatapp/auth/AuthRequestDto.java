package com.alibu.chatapp.auth;

public record AuthRequestDto(
        String email,
        String password
) {
}
