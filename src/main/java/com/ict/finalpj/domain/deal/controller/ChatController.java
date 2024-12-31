package com.ict.finalpj.domain.deal.controller;

import java.util.ArrayList;
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
import com.ict.finalpj.domain.deal.vo.ChatVO;
import com.ict.finalpj.domain.user.service.UserService;
import com.ict.finalpj.domain.user.vo.UserVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/chat")
public class ChatController {
    
    @Autowired
    private ChatService chatService;

    @Autowired
    private UserService userService;
    
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

            List<UserVO> userList = new ArrayList<>();

            List<ChatVO> recentChat = new ArrayList<>();
            
            for (ChatRoomVO k : chatList) {
                // 사용자 Idx, nickname, 평점 가져오기
                UserVO uvo = userService.getUserInfoByIdx(k.getUserIdx());
                UserVO setUvo = new UserVO();
                setUvo.setUserNickname(uvo.getUserNickname());
                setUvo.setUserIdx(uvo.getUserIdx());
                setUvo.setDealSatisSellerScore(uvo.getDealSatisSellerScore());
                userList.add(setUvo);

                ChatVO chatvo = new ChatVO();
                ChatVO cvo_true = chatService.getRecentChat(k);

                if(cvo_true == null){
                    chatvo.setChatRoom(k.getChatRoom());
                    recentChat.add(chatvo);
                }else{
                    recentChat.add(cvo_true);
                }

            }

            if(!sellerIdx.equals("null")){

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
            }

            log.info("chatList : " + chatList);
            log.info("userList : " + userList);
            log.info("recentChat : " + recentChat);

            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("chatList", chatList);
            resultMap.put("userList", userList);
            resultMap.put("recentChat", recentChat);

            dvo.setData(resultMap);
            dvo.setSuccess(true);
            dvo.setMessage("채팅 정보 가져오기 성공");

            // lastRead 시간 업데이트 하기
            
        } catch (Exception e) {
            dvo.setSuccess(false);
            dvo.setMessage("채팅 리스트 불러오기 오류 발생");
            e.printStackTrace();
        }

        return dvo;
    }
    
    @GetMapping("/getUnReadMessages")
    public DataVO getUnReadMessages(
        @RequestParam("userIdx") String userIdx
        ) {
        DataVO dvo = new DataVO();
        try {
            // 안 읽은 메시지 수 가져오기
            int unReadCnt = chatService.getUnReadMessages(userIdx);

            dvo.setData(unReadCnt);
            dvo.setSuccess(true);
            dvo.setMessage("안 읽은 메시지 수 조회 성공");
        } catch (Exception e) {
            e.printStackTrace();
            dvo.setSuccess(false);
            dvo.setMessage("안 읽은 메시지 수 조회 실패!");
        }
        return dvo;
    }
    
}
