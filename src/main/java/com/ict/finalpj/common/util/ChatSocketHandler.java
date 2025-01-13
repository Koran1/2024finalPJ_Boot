package com.ict.finalpj.common.util;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.HandshakeData;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.ict.finalpj.domain.deal.service.ChatService;
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


    @Autowired
    private ChatService chatService;
    public ChatSocketHandler(SocketIOServer server, SocketService socketService, JwtUtil jwtUtil, MyUserDetailService myUserDetailService) {
        this.server = server;
        this.socketService = socketService;
        this.jwtUtil = jwtUtil;
        this.myUserDetailService = myUserDetailService;
        server.addConnectListener(onConnected());
        server.addDisconnectListener(onDisconnected());
        server.addEventListener("send_message", ChatVO.class, onChatReceived());
        server.addEventListener("first_message", ChatVO.class, onFirstChat());
    }

    private DataListener<ChatVO> onFirstChat() {
        return (senderClient, data, ackSender) -> {

            LocalDateTime now = LocalDateTime.now();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            String formattedTimestamp = now.format(formatter);

            data.setChatTime(formattedTimestamp);
            
            // DB에 채팅 내용 저장
            socketService.saveChatMessage(data);

            // 메세지 알림
            server.getRoomOperations(data.getChatRoom()).sendEvent("receive_message", data);
        };
    }

    private DataListener<ChatVO> onChatReceived() {
        return (senderClient, data, ackSender) -> {

            LocalDateTime now = LocalDateTime.now();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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
                String userIdx = handshakeData.getSingleUrlParam("userIdx");
                UserDetails userDetails = myUserDetailService.loadUserByUsername(userIdx);
                
                String jwtToken = handshakeData.getSingleUrlParam("token");
                if(jwtUtil.validateToken(jwtToken, userDetails)) {
                    
                    client.joinRoom(room);

                    // chatList 정보 가져오기
                    List<ChatVO> chatList = socketService.getChatList(room);
                    client.sendEvent("chatList", chatList);

                    // lastRead 시간 갱신
                    Map<String, String> map = new HashMap<>();
                    map.put("chatRoom", room);
                    map.put("userIdx", userIdx);
                    
                    chatService.updateLastRead(map);

                }else {
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
            HandshakeData handshakeData = client.getHandshakeData();
            String room = handshakeData.getSingleUrlParam("room");
            String userIdx = handshakeData.getSingleUrlParam("userIdx");

            client.leaveRoom(room);
            Map<String, String> map = new HashMap<>();
            map.put("chatRoom", room);
            map.put("userIdx", userIdx);
            chatService.updateLastRead(map);
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
