package com.alibu.chatapp.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
//@CrossOrigin(origins = "http://localhost:3000")
public class ChatController {


    //    @MessageMapping("/message")//the client will send the message to this endpoint
//    @SendTo("/topic/return-to")//All the client that are subscribed to this endpoint will receive the message
//    public Message getContent (@RequestBody Message message) {
//        try{
//            //Todo
//            //save the message to the database
//            //get message from the database
//            Thread.sleep(1000);
//        }
//        catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        return message;
//    }
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    private final ChatMessageService chatMessageService;

    @MessageMapping("/message")
    @SendTo("/chatroom/public")
    public ChatMessage receiveMessage(@RequestBody ChatMessage message) {
        return message;
    }

    @MessageMapping("/private-message")// /chat
    public ChatMessage recMessage(@RequestBody ChatMessage message) {
        simpMessagingTemplate.convertAndSendToUser(message.getReceiverWebId(), "/private", message);
        System.out.println(message.toString());
        return message;
    }

    @MessageMapping("/chat")
    public void processMessage(@RequestBody ChatMessage chatMessage) {
        ChatMessage savedMsg = chatMessageService.save(chatMessage);
        simpMessagingTemplate.convertAndSendToUser(
                chatMessage.getReceiverWebId(), "/queue/messages",
                new ChatNotification(
                        savedMsg.getSenderWebId(),
                        savedMsg.getReceiverWebId(),
                        savedMsg.getContent(),
                        savedMsg.getType()
                )
        );
    }



    @GetMapping("/messages/{senderWebId}/{receiverWebId}")
    public ResponseEntity<List<ChatNotification>> findChatMessage(
            @PathVariable("senderWebId") String senderWebId,
            @PathVariable("receiverWebId") String receiverWebId
    ) {
        return ResponseEntity.ok(chatMessageService.findChatMessages(senderWebId, receiverWebId));
    }

    @GetMapping("/messages/all/{senderWebId}")
    public ResponseEntity<List<ChatTable>> findAllChatMessage(
            @PathVariable("senderWebId") String senderWebId
    ) {
        return ResponseEntity.ok(chatMessageService.findAllChatMessages(senderWebId));
    }
}