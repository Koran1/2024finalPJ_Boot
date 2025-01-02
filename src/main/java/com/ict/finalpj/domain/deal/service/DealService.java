package com.ict.finalpj.domain.deal.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ict.finalpj.common.vo.FileVo;
import com.ict.finalpj.common.vo.ViewsVO;
import com.ict.finalpj.domain.deal.vo.DealFavoriteVO;
import com.ict.finalpj.domain.deal.vo.DealSatisfactionVO;
import com.ict.finalpj.domain.deal.vo.DealVO;
import com.ict.finalpj.domain.user.vo.UserVO;

@Service
public interface DealService {
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

    int getDealStatusUpdate(String dealIdx, String status);

    // 판매자의 다른 상품 조회
    List<DealVO> getSellerOtherDeals(String dealSellerUserIdx, String dealIdx);

    // 만족도 평가 저장
    int getDealSatisfactionInsert(DealSatisfactionVO satisfactionVO);

    // 판매자의 평점 조회
    String getDealSatisSellerScore(String dealSellerUserIdx);

    // 판매자 평점 업데이트
    void getDealSatisSellerScoreUpdate(String dealSellerUserIdx);

    int getDealActiveUpdate(String dealIdx, int dealview);
} 