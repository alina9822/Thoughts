package com.alibu.chatapp.chat;


import com.alibu.chatapp.chatRoom.ChatRoomService;
import com.alibu.chatapp.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
    private final ChatMessageRepository repository;
    private final ChatRoomService chatRoomService;
    private final UserService userService;

    public ChatMessage save(ChatMessage chatMessage) {
        var chatId = chatRoomService.getChatRoomId(
                chatMessage.getSenderWebId(),
                chatMessage.getReceiverWebId(),
                true
        ).orElseThrow();///so I have to use a try catch block where this function is used

        chatMessage.setChatId(chatId);
        repository.save(chatMessage);
        return chatMessage;
    }

    public List<ChatNotification> findChatMessages(
            String senderWebId, String receiverWebId) {
        var chatId = chatRoomService.getChatRoomId(
                senderWebId,
                receiverWebId,
                false);
        List<ChatMessage> chatMessages = chatId.map(repository::findByChatId)
                .orElse(new ArrayList<>());///so I have to use a try catch block where this function is used
        List<ChatNotification> chatNotifications = new ArrayList<>();
        for (var chatMessage : chatMessages) {
            chatNotifications.add(ChatNotification.builder()
                    .senderWebId(chatMessage.getSenderWebId())
                    .receiverWebId(chatMessage.getReceiverWebId())
                    .content(chatMessage.getContent())
                    .build());
        }
        return chatNotifications;
    }


    public List<ChatTable> findAllChatMessages(String senderWebId) {
        var users = userService.getAllUsers();
        List<ChatTable> chatTable = new ArrayList<>();
        for (var user : users) {
            chatTable.add(ChatTable.builder()
                    .receiverWebId(user.webId())
                    .messages(findChatMessages(senderWebId, user.webId()))
                    .build());
        }
        return chatTable;
    }
}
