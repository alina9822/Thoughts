package com.alibu.chatapp.chatRoom;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "chat_room")
public class ChatRoom {
    @Id
    @GeneratedValue
    private Long id;
    private String chatId;
    private String senderWebId;//sender username
    private String receiverWebId;//receiver username
}
