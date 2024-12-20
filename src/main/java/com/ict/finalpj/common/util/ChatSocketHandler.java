package com.ict.finalpj.common.util;

import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.ict.finalpj.domain.deal.service.SocketService;
import com.ict.finalpj.domain.deal.vo.MessageVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ChatSocketHandler {

    private final SocketIOServer server;
    private final SocketService socketService;

    public ChatSocketHandler(SocketIOServer server, SocketService socketService) {
        this.server = server;
        this.socketService = socketService;
        server.addConnectListener(onConnected());
        server.addDisconnectListener(onDisconnected());
        server.addEventListener("send_message", MessageVO.class, onChatReceived());
    }

    private DataListener<MessageVO> onChatReceived() {
        return (senderClient, data, ackSender) -> {
            log.info(data.toString());
            socketService.sendMessage(data.getRoom(), "get_message", senderClient, data.getMessage());
        };
    }

    private ConnectListener onConnected() {
        return (client) -> {
            String room = client.getHandshakeData().getSingleUrlParam("room");
            log.info("room num : " +room); 
            log.info("client.getSessionId().toString() : " + client.getSessionId().toString()); 
            client.joinRoom(room);
            log.info("Socket ID[{}]  Connected to socket", client.getSessionId().toString());
        };
    }
    
    private DisconnectListener onDisconnected() {
        return client -> {
            String room = client.getHandshakeData().getSingleUrlParam("room");
            log.info(room);
            client.leaveRoom(room);
            log.info("Client[{}] - Disconnected from socket", client.getSessionId().toString());
        };
    }
}
