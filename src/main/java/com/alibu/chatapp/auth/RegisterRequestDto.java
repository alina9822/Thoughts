package com.alibu.chatapp.auth;

import com.alibu.chatapp.user.Role;

public record RegisterRequestDto(
        String firstname,
        String lastname,
        String email,
        String password,
        Role role
) {
}
