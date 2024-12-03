package com.alibu.chatapp.user;

import lombok.Builder;

@Builder
public record UserResponseDto(
        String email,
        String firstname,
        String lastname,
        String webId,
        Status state
) {
}
