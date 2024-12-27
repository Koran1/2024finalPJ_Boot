package com.ict.finalpj.domain.deal.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ict.finalpj.common.vo.FileVo;
import com.ict.finalpj.common.vo.ViewsVO;
import com.ict.finalpj.domain.deal.vo.DealFavoriteVO;
import com.ict.finalpj.domain.deal.vo.DealVO;

@Service
public interface DealService {
  List<DealVO> getDealMainList();
  FileVo getDealFileOne(String fileTableIdx);

  DealVO getDealDetail(String dealIdx);
  List<FileVo> getDealFileDetail(String fileTableIdx);

  int getDealWrite(DealVO dealVO);
  int getIDealFileInsert(FileVo fileVo);
  
  int getDealFileDelete(FileVo fileVo);
  int getDealFileUpdate(FileVo fileVo);
  int getDealUpdate(DealVO dealVO);
  
  List<DealVO> getDealManagement(String userIdx);
  List<DealFavoriteVO> getDealinterest(String userIdx);

  int getDealFileNameDelete(String fileTableIdx, String fileName);
  int getDealFileOrder(FileVo fileVo);

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