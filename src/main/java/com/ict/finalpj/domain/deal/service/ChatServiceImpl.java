package com.ict.finalpj.domain.deal.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ict.finalpj.domain.deal.mapper.ChatMapper;
import com.ict.finalpj.domain.deal.vo.ChatRoomVO;

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



}
