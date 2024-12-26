package com.ict.finalpj.domain.deal.service;

import java.util.List;
import java.util.Map;

import com.ict.finalpj.domain.deal.vo.ChatRoomVO;

public interface ChatService {
    public ChatRoomVO chkByUserIdx(Map<String, String> map);
    public int insertNewChat(Map<String, String> map);
    public List<ChatRoomVO> getChatListByUserIdx(String userIdx);

}
