package com.alibu.chatapp.chatRoom;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom,Long> {

    Optional<ChatRoom> findBySenderWebIdAndReceiverWebId(String senderWebId, String receiverWebId);
}
