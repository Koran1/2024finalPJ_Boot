package com.ict.finalpj.common.util;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.HandshakeData;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.ict.finalpj.domain.deal.service.SocketService;
import com.ict.finalpj.domain.deal.vo.ChatVO;
import com.ict.finalpj.domain.user.service.MyUserDetailService;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ChatSocketHandler {

    private final SocketIOServer server;
    private final SocketService socketService;
    private final JwtUtil jwtUtil;
    private final MyUserDetailService myUserDetailService;

    public ChatSocketHandler(SocketIOServer server, SocketService socketService, JwtUtil jwtUtil, MyUserDetailService myUserDetailService) {
        this.server = server;
        this.socketService = socketService;
        this.jwtUtil = jwtUtil;
        this.myUserDetailService = myUserDetailService;
        server.addConnectListener(onConnected());
        server.addDisconnectListener(onDisconnected());
        server.addEventListener("send_message", ChatVO.class, onChatReceived());
    }

    private DataListener<ChatVO> onChatReceived() {
        return (senderClient, data, ackSender) -> {
            log.info("data" + data);
            log.info("Chat Received : " + data.getChatMessage());

            // Get the current date and time
            LocalDateTime now = LocalDateTime.now();

            // Define the format for the timestamp
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            // Format the current date and time
            String formattedTimestamp = now.format(formatter);

            data.setChatTime(formattedTimestamp);
            
            // DB에 채팅 내용 저장
            socketService.saveChatMessage(data);


            // 메세지 알림
            server.getRoomOperations(data.getChatRoom()).sendEvent("receive_message", data);
        };
    }

    private ConnectListener onConnected() {
        return (client) -> {
            try {
                HandshakeData handshakeData = client.getHandshakeData();
                String room = handshakeData.getSingleUrlParam("room");
                log.info("room num : " +room); 
                
                String userIdx = handshakeData.getSingleUrlParam("userIdx");
                log.info("user : " + userIdx);
                UserDetails userDetails = myUserDetailService.loadUserByUsername(userIdx);
                
                String jwtToken = handshakeData.getSingleUrlParam("token");
                if(jwtUtil.validateToken(jwtToken, userDetails)) {
                    log.info("jwt token is valid");
                    log.info("client jwt token : " + jwtToken); 
                    
                    client.joinRoom(room);
                    log.info("Socket ID[{}]  Connected to socket", client.getSessionId().toString());

                    List<ChatVO> chatList = socketService.getChatList(room);
                    client.sendEvent("chatList", chatList);
                    
                }else {
                    log.info("jwt token is invalid");
                    client.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
                client.disconnect();
            }

        };
    }
    
    private DisconnectListener onDisconnected() {
        return client -> {
            String room = client.getHandshakeData().getSingleUrlParam("room");
            log.info("user leaves room num : " +room);
            client.leaveRoom(room);
            log.info("Client[{}] - Disconnected from socket", client.getSessionId().toString());
        };
    }

    @PreDestroy
    public void shutdownSocketServer() {
        if (server != null) {
            log.info("Shutting down Socket.IO server...");
            server.stop();
        }
    }
}
