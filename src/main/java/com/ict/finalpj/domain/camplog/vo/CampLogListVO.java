package com.ict.finalpj.domain.camplog.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CampLogListVO {
    private String 
        logIdx, // 캠프로그 제목 고유번호
        userIdx, // 사용자 고유번호
        userNickname, // 사용자 닉네임
        campIdx, // 캠핑장 고유번호
        facltNm, // 캠핑장명
        logTitle, // 후기 제목
        logView, // 후기 조회수
        logRecommend, // 후기 공감수
        logIsActive, // 후기 제목 활성화 여부
        logUpdateDate, // 후기 수정날짜
        logRegDate, // 후기 등록날짜
        logContentIdx, // 캠프로그 내용 고유번호
        logContent, // 후기 내용
        logContentIsActive, // 후기 내용 활성화 여부 
        fileIdx, // 사진 파일 고유번호
        fileTableType, // 파일 메뉴 타입(1인 경우 캠핑로그)
        fileTableIdx, // fileTableIdx = logIdx
        fileName, // 파일명
        fileUpdated, // 파일 수정날짜
        fileCreated, // 파일 등록날짜
        fileOrder, // 대표사진 = 1
        fileActive, // 사진 활성화 여부
        isThumbnail, // 썸네일
        logRecommendIdx, // 후기 공감 고유번호
        logCommentIdx, // 후기 댓글 고유번호
        logCommentIsActive, // 후기 댓글 활성화 여부
        reportIdx, // 신고글 고유번호
        // reportedUserIdx, // 피신고자 고유번호
        reportTableType, // 신고 테이블 종류(3인 경우에, 후기 글 신고)
        reportTableIdx, // reportTableIdx = logIdx
        // reportStatus, // 신고 처리 상태(0인 경우, 처리 전 / 1인 경우 처리 완료)
        // reporterUserIdx, 
        reporterUserIds
        ;

        private String keyword, option, sortOption;
        private int page, size, offset, totalLikes, totalCount, reportCount, reportStatus, reporterUserIdx, reportedUserIdx;
}
