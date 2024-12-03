package com.alibu.chatapp.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatNotification {
    private String senderWebId;
    private String receiverWebId;
    private String content;
    private Type type;
}
