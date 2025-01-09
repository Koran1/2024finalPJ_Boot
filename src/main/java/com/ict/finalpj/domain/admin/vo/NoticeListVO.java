package com.ict.finalpj.domain.admin.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoticeListVO {
    private String 
        noticeIdx, 
        noticeSubject, 
        noticeFile, 
        noticeContent, 
        noticeReg, 
        noticeLevel, 
        noticeStatus, 
        noticeEtc01, 
        noticeEtc02, 
        keyword, 
        option,
        selectedNotices
        ;
    private int page, size, offset;
}
