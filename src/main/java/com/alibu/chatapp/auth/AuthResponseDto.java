package com.alibu.chatapp.auth;


import com.alibu.chatapp.user.Role;
import lombok.Builder;

@Builder
public record AuthResponseDto(
        String token,
        String firstname,
        String lastname,
        String email,
        String webId,
        Role role
) {
}
