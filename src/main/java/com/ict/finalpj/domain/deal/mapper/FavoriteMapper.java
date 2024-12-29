package com.ict.finalpj.domain.deal.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.ict.finalpj.common.vo.ViewsVO;

@Mapper
public interface FavoriteMapper {
    // 좋아요 상태 확인
    boolean isLiked(String userIdx, String dealIdx);

    // 좋아요 추가
    int likeDeal(String userIdx, String dealIdx);
    
    // 좋아요 삭제
    int unlikeDeal(String userIdx, String dealIdx);
    
    // 조회수 관련 메서드
    ViewsVO getViewCount(String userIdx, String dealIdx);
    int insertViewCount(String userIdx, String dealIdx);
    int updateViewCount(String userIdx, String dealIdx);
} 