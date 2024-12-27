package com.ict.finalpj.domain.deal.vo;

import lombok.Data;

@Data
public class ChatRoomVO {
    private String chatRoom;
    private String userIdx;
    private String lastRead;
    private String unReadCount;
}
