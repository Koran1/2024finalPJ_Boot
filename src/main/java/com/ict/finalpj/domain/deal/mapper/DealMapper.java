package com.ict.finalpj.domain.deal.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ict.finalpj.common.vo.FileVo;
import com.ict.finalpj.common.vo.ViewsVO;
import com.ict.finalpj.domain.deal.vo.DealFavoriteVO;
import com.ict.finalpj.domain.deal.vo.DealVO;
import com.ict.finalpj.domain.user.vo.UserVO;

@Mapper
public interface DealMapper {
    // 기존 Deal 관련 메서드들
    List<DealVO> getDealMainList();
    FileVo getDealFileOne(String fileTableIdx);
    DealVO getDealDetail(String dealIdx);
    List<FileVo> getDealFileDetail(String fileTableIdx);
    int getDealWrite(DealVO dealVO);
    int getIDealFileInsert(FileVo fileVo);
    int getDealUpdate(DealVO dealVO);
    int getDealFileUpdate(FileVo fileVo);
    int getDealFileDelete(FileVo fileVo);
    List<DealVO> getDealManagement(String userIdx);
    int getDealFileNameDelete(String fileTableIdx, String fileName);
    int getDealFileOrder(FileVo fileVo);

    // DealFavorite 관련 메서드들
    List<DealFavoriteVO> getDealinterest(String userIdx);
    boolean isLiked(String userIdx, String dealIdx);
    int likeDeal(String userIdx, String dealIdx);
    int unlikeDeal(String userIdx, String dealIdx);
    int getFavoriteCount(String dealIdx);
    
    // 조회수 관련 메서드들
    ViewsVO getViewCount(String userIdx, String dealIdx);
    int insertViewCount(String userIdx, String dealIdx);
    int updateViewCount(String userIdx, String dealIdx);

    // 판매자 정보 조회 
    UserVO getUserInfoByIdx(String userIdx);

    // 총 조회수 조회
    int getTotalViewCount(String dealIdx);
}
