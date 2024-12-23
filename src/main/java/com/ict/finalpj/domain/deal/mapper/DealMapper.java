package com.ict.finalpj.domain.deal.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ict.finalpj.common.vo.FileVo;
import com.ict.finalpj.domain.deal.vo.DealVO;

@Mapper
public interface DealMapper {
  List<DealVO> getDealMainList();
  DealVO getDealDetail(String dealIdx);
  int getDealUpdate(DealVO dealVO);
  int getDealWrite(DealVO dealVO);
  void insertFile(FileVo fileVO);
  void updateFile(FileVo fileVO);
  void insertFileInfo(FileVo fileVo);
  List<FileVo> getPjFileByDealIdx(String dealIdx);
  int updateDeal(DealVO dealVO);
  
  DealVO getDealManagement(String userIdx);
  
}