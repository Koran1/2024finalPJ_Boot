package com.ict.finalpj.domain.deal.service;

import org.springframework.stereotype.Service;

import com.corundumstudio.socketio.SocketIOClient;
import com.ict.finalpj.domain.deal.vo.MessageVO;

@Service
public class SocketService {
    public void sendMessage(String room, String eventName, SocketIOClient senderClient, String message) {
        for (
                SocketIOClient client : senderClient.getNamespace().getRoomOperations(room).getClients()) {
            if (!client.getSessionId().equals(senderClient.getSessionId())) {
                client.sendEvent(eventName,
                        new MessageVO(MessageVO.MessageType.SERVER, message));
            }
        }
    }
}
