package com.ict.finalpj.domain.deal.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ict.finalpj.domain.deal.mapper.ChatMapper;
import com.ict.finalpj.domain.deal.vo.ChatRoomVO;
import com.ict.finalpj.domain.deal.vo.ChatVO;

@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    private ChatMapper chatMapper;
    
    @Override
    public ChatRoomVO chkByUserIdx(Map<String, String> map) {
        return chatMapper.chkByUserIdx(map);
    }

    @Override
    public int insertNewChat(Map<String, String> map) {
        return chatMapper.insertNewChat(map);
    }

    @Override
    public List<ChatRoomVO> getChatListByUserIdx(String userIdx) {
        return chatMapper.getChatListByUserIdx(userIdx);
    }

    @Override
    public ChatVO getRecentChat(ChatRoomVO chatRoom) {
        return chatMapper.getRecentChat(chatRoom);
    }

    @Override
    public int getUnReadMessages(String userIdx) {
        return chatMapper.getUnReadMessages(userIdx);
    }

    @Override
    public void updateLastRead(Map<String, String> map) {
        chatMapper.updateLastRead(map);
    }



}
