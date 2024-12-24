package com.ict.finalpj.domain.camp.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ict.finalpj.common.vo.ViewsVO;
import com.ict.finalpj.domain.camp.vo.CampVO;
import com.ict.finalpj.domain.camplog.vo.CampLogVO;

@Mapper
public interface CampMapper2 {
    // 캠핑장 상세 정보
    CampVO getCampDetail(String campIdx);

    // 캠핑로그 목록
    List<CampLogVO> getCampLog(@Param("campIdx") String campIdx, @Param("logAlign") String logAlign);

    // 좋아요 상태 조회
    boolean isLiked(@Param("userIdx") String userIdx, @Param("campIdx") String campIdx);

    // 좋아요 추가
    void addLike(@Param("userIdx") String userIdx, @Param("campIdx") String campIdx);

    // 좋아요 삭제
    void removeLike(@Param("userIdx") String userIdx, @Param("campIdx") String campIdx);

    ViewsVO getViewInfo(@Param("userIdx") String userIdx, @Param("campIdx") String campIdx);

    void insertViewInfo(@Param("userIdx") String userIdx, @Param("campIdx") String campIdx);

    void updateViewInfo(@Param("userIdx") String userIdx, @Param("campIdx") String campIdx);
}
