package com.ict.finalpj.domain.camp.service;

import java.util.List;

import com.ict.finalpj.domain.camp.vo.CampFavVO;
import com.ict.finalpj.domain.camp.vo.CampVO;
import com.ict.finalpj.domain.camplog.vo.CampLogVO;

public interface CampService2 {
    // 캠핑장 상세 정보
    CampVO getCampDetail(String campIdx);

    // 캠핑로그 목록
    List<CampLogVO> getCampLog(String campIdx, String logAlign);

    // 좋아요 상태 확인
    boolean isLiked(String userIdx, String campIdx);

    // 좋아요 추가
    void likeCamp(String userIdx, String campIdx);

    // 좋아요 삭제
    void unlikeCamp(String userIdx, String campIdx);

    // 캠프 조회수 메서드
    void updateViewCount(String userIdx, String campIdx);

    // 캠핑장 좋아요 리스트 조회
    List<CampFavVO> getLikeList(String userIdx);
}
