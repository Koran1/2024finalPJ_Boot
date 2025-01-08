package com.ict.finalpj.domain.deal.service;

import java.util.List;
import java.util.Map;

import com.ict.finalpj.domain.deal.vo.ChatRoomVO;
import com.ict.finalpj.domain.deal.vo.ChatVO;

public interface ChatService {
    public ChatRoomVO chkByUserIdx(Map<String, String> map);
    public int insertNewChat(Map<String, String> map);
    public List<ChatRoomVO> getChatListByUserIdx(String userIdx);

    public ChatVO getRecentChat(ChatRoomVO chatRoom);
    public int getUnReadMessages(String userIdx);

    public void updateLastRead(Map<String, String> map);
    int leaveChat(ChatRoomVO crvo);
}
