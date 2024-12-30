package com.ict.finalpj.domain.mycamp.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyLogVO {
    private String 
        logIdx, // 캠프로그 고유번호
        userIdx, // 사용자 고유번호
        logIsActive, // 캠핑로그 글 활성화 여부
        logTitle, // 캠핑로그 제목
        logCommentIdx, // 댓글 및 답글 고유번호
        logView, // 캠핑로그 조회수
        logRecommendIdx, // 후기 글 추천 고유번호
        logRecommend, // 후기글 추천수
        logUpdateDate, // 캠핑로그 수정날짜
        logRegDate, // 캠핑로그 작성날짜
        fileIdx, // 사진 파일 고유번호
        fileTableType, // 파일 메뉴 타입(1인 경우 캠핑로그)
        fileTableIdx, // fileTableIdx = logIdx
        fileName // 파일명
        ;

    private int page, size, offset, commentCount;
}
