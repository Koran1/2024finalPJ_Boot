package com.ict.finalpj.domain.add.vo;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class QNAVO {
    private String qnaIdx, userIdx, qnaSubject, qnaFile, qnaContent,
    qnaRegDate, qnaReSubject, qnaReContent, qnaReRegDate, qnaStatus, qnaEtc01, qnaEtc02;
}
