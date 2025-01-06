package com.ict.finalpj.domain.deal.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.corundumstudio.socketio.SocketIOClient;
import com.ict.finalpj.domain.deal.mapper.ChatMapper;
import com.ict.finalpj.domain.deal.vo.ChatVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SocketService {

    @Autowired
    private ChatMapper chatMapper;

    public int saveChatMessage(ChatVO chatvo) {
        return chatMapper.insertChatMessage(chatvo);
    }

    public List<ChatVO> getChatList(String chatRoom) {
        return chatMapper.getChatList(chatRoom);
    }

}
