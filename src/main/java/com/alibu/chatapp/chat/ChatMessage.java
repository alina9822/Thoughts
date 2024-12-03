package com.alibu.chatapp.chat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table
public class ChatMessage {

    @Id
    @GeneratedValue
    private Long id;
    private String chatId;
    private String senderWebId;
    private String receiverWebId;
    private String content;
    private Date timestamp;
    private Type type;
//    private String name;
//    private String content;
}