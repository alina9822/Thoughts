package com.alibu.chatapp.chat;

import lombok.Builder;

import java.util.List;

@Builder
public record ChatTable(
        String receiverWebId,
        List<ChatNotification> messages
) {
}
