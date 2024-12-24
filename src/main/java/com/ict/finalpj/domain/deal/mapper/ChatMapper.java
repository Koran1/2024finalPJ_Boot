package com.ict.finalpj.domain.deal.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ict.finalpj.domain.deal.vo.ChatVO;

@Mapper
public interface ChatMapper {
    int insertChatMessage(ChatVO chatvo);
    
    List<ChatVO> getChatList(String chatRoom);
}
