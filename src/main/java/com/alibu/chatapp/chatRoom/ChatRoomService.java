package com.alibu.chatapp.chatRoom;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

//
@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;

    public Optional<String> getChatRoomId(
            String senderWebId,
            String receiverWebId,
            boolean createIfNotExist
    ) {
        return chatRoomRepository
                .findBySenderWebIdAndReceiverWebId(senderWebId, receiverWebId)//this portion will return a chatroom
                .map(ChatRoom::getChatId)//this portion will return the chatId
                .or(() -> {//if they don't find any chatroom then this part will run
                    if (createIfNotExist) {
                        var chatId = createChatId(senderWebId, receiverWebId);
                        return Optional.of(chatId);
                    }
                    return Optional.empty();
                });
    }

    private String createChatId(String senderWebId, String receiverWebId) {
        var chatId = String.format("%s_%s", senderWebId, receiverWebId);

        ChatRoom senderRecipient = ChatRoom.builder()
                .chatId(chatId)
                .senderWebId(senderWebId)
                .receiverWebId(receiverWebId)
                .build();
        chatRoomRepository.save(senderRecipient);

        if(senderWebId.equals(receiverWebId)) {
            return chatId;
        }
        ChatRoom RecipientSender = ChatRoom.builder()
                .chatId(chatId)
                .senderWebId(receiverWebId)
                .receiverWebId(senderWebId)
                .build();
        chatRoomRepository.save(RecipientSender);

        return chatId;
    }


}