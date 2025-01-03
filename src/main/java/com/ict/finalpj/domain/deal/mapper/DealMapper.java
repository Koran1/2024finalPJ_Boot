package com.ict.finalpj.domain.deal.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ict.finalpj.common.vo.FileVo;
import com.ict.finalpj.common.vo.ViewsVO;
import com.ict.finalpj.domain.deal.vo.DealFavoriteVO;
import com.ict.finalpj.domain.deal.vo.DealSatisfactionVO;
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

  int getDealFileNameDelete(String fileTableIdx, String fileName);

  int getDealFileOrder(FileVo fileVo);

  // DealFavorite 관련 메서드들
  boolean isLiked(@Param("userIdx") String userIdx, @Param("dealIdx") String dealIdx);
  int likeDeal(@Param("userIdx") String userIdx, @Param("dealIdx") String dealIdx);
  int unlikeDeal(@Param("userIdx") String userIdx, @Param("dealIdx") String dealIdx);
  int getFavoriteCount(String dealIdx);
  
  // 조회수 관련 메서드들
  ViewsVO getViewCount(@Param("userIdx") String userIdx, @Param("dealIdx") String dealIdx);
  int insertViewCount(@Param("userIdx") String userIdx, @Param("dealIdx") String dealIdx);
  int updateViewCount(@Param("userIdx") String userIdx, @Param("dealIdx") String dealIdx);

  // 판매자 정보 조회 
  UserVO getUserInfoByIdx(String userIdx);
  
  // 총 조회수 조회
  int getTotalViewCount(String dealIdx);
  
  // 판매 상태 변경
  int getDealStatusUpdate(@Param("dealIdx") String dealIdx, @Param("status") String status);
  
  // 판매자의 다른 상품 조회
  List<DealVO> getSellerOtherDeals(@Param("dealSellerUserIdx") String dealSellerUserIdx, @Param("dealIdx") String dealIdx);
  
  // 만족도 평가 저장
  int getDealSatisfactionInsert(DealSatisfactionVO satisfactionVO);

  // 판매자의 평점 조회
  String getDealSatisSellerScore(String dealSellerUserIdx);
  
  // 상품 활성화 상태 업데이트
  int getDealActiveUpdate(@Param("dealIdx") String dealIdx, @Param("dealview") int dealview);



  List<DealFavoriteVO> getDealinterest(String userIdx);
  List<DealVO> getDealManagement(String userIdx);
  List<DealVO> getDealMainSearch(String searchKeyword);

  List<DealVO> getFavoriteList(String userIdx);
}
