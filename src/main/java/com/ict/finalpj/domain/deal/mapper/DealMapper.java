package com.ict.finalpj.domain.deal.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ict.finalpj.common.vo.FileVo;
import com.ict.finalpj.domain.deal.vo.DealFavoriteVO;
import com.ict.finalpj.domain.deal.vo.DealVO;

@Mapper
public interface DealMapper {
  List<DealVO> getDealMainList();
  FileVo getDealFileOne(String fileTableIdx);  // 파일 메인만 조회

  DealVO getDealDetail(String dealIdx);
  List<FileVo> getDealFileDetail(String fileTableIdx);  // 파일 조회

  int getDealWrite(DealVO dealVO);
  int getIDealFileInsert(FileVo fileVo); // 파일 추가
  
  int getDealUpdate(DealVO dealVO);
  int getDealFileUpdate(FileVo fileVo); // 파일 수정
  int getDealFileDelete(FileVo fileVo); // 파일 삭제
  
  List<DealVO> getDealManagement(String userIdx);
  List<DealFavoriteVO> getDealinterest(String userIdx); 

  int getDealFileNameDelete(String fileTableIdx, String fileName);
  int getDealFileOrder(FileVo fileVo); // 파일 순서 매칭
}
