package com.ict.finalpj.domain.deal.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageVO {
    private MessageType type;
    private String message;
    private String room;

    public MessageVO(MessageType type, String message) {
        this.type = type;
        this.message = message;
    }

    public enum MessageType{
        SERVER, CLIENT;
    }
}