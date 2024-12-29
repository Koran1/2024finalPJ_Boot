package com.ict.finalpj.domain.add.vo;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class NoticeVO {
    private String noticeIdx, noticeSubject, noticeFile, noticeContent,
    noticeReg, noticeLevel, noticeStatus, noticeEtc01, noticeEtc02;
    private MultipartFile noticeMultipartFile; 
}
