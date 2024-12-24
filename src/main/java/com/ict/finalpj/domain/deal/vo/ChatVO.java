package com.ict.finalpj.domain.deal.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatVO {
    private String chatIdx;
    private String chatRoom;
    private String chatSenderIdx;
    private String chatMessage;
    private String chatTime;

    // public ChatVO(String chatSenderIdx, String message) {
        
    // }

    public enum MessageType{
        SERVER, CLIENT;
    }
}