package com.ict.finalpj.domain.deal.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ict.finalpj.common.vo.DataVO;
import com.ict.finalpj.domain.deal.service.ChatService;
import com.ict.finalpj.domain.deal.service.SocketService;
import com.ict.finalpj.domain.deal.vo.ChatRoomVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/chat")
public class ChatController {
    
    @Autowired
    private ChatService chatService;

    @Autowired
    private SocketService socketService;

    @GetMapping("/getChatList")
    public DataVO getChatList(
        @RequestParam("sellerIdx") String sellerIdx,
        @RequestParam("userIdx") String userIdx) {
        DataVO dvo = new DataVO();
        try {
            log.info("userIdx : "+userIdx);
            log.info("sellerIdx : "+sellerIdx);
            
            List<ChatRoomVO> chatList = chatService.getChatListByUserIdx(userIdx);

            // 신규 채팅인지 확인하기
            Map<String, String> map = new HashMap<>();
            map.put("userIdx", userIdx);
            map.put("sellerIdx", sellerIdx);
            ChatRoomVO chkRoom = chatService.chkByUserIdx(map);
            log.info("chkRoom" + chkRoom);

            if(chkRoom == null){
                log.info("User is new");
                // add chats
                String chatRoom = UUID.randomUUID().toString();
                map.put("chatRoom", chatRoom);
                int result = chatService.insertNewChat(map);
                if(result == 0){
                    dvo.setSuccess(false);
                    dvo.setMessage("채팅 생성 실패!");
                    return dvo;
                }else{
                    chkRoom.setChatRoom(chatRoom);
                    chkRoom.setUserIdx(sellerIdx);
                    chatList.add(chkRoom);
                }
            }else{
                log.info("User is already in that chatting");
                // load chats
            }
            log.info("chatList : " + chatList);
            dvo.setData(chatList);
            dvo.setSuccess(true);
            dvo.setMessage("채팅 정보 가져오기 성공");

        } catch (Exception e) {
            dvo.setSuccess(false);
            dvo.setMessage("채팅 리스트 불러오기 오류 발생");
            e.printStackTrace();
        }

        return dvo;
    }
    
}
