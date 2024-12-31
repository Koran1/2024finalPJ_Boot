package com.ict.finalpj.domain.deal.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.ict.finalpj.domain.deal.vo.ChatRoomVO;
import com.ict.finalpj.domain.deal.vo.ChatVO;

@Mapper
public interface ChatMapper {
    int insertChatMessage(ChatVO chatvo);
    
    List<ChatVO> getChatList(String chatRoom);

    ChatRoomVO chkByUserIdx(Map<String, String> map);
    int insertNewChat(Map<String, String> map);
    
    List<ChatRoomVO> getChatListByUserIdx(String userIdx);

    ChatVO getRecentChat(ChatRoomVO chatRoom);
    int getUnReadMessages(String userIdx);

    void updateLastRead(Map<String, String> map);
}
